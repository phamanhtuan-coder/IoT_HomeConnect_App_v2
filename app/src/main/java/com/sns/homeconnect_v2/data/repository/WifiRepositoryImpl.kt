package com.sns.homeconnect_v2.data.repository

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.net.wifi.ScanResult
import android.net.wifi.WifiManager
import android.net.wifi.WifiNetworkSpecifier
import android.os.Build
import android.os.Handler
import android.os.Looper
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import com.sns.homeconnect_v2.core.permission.PermissionManager
import com.sns.homeconnect_v2.core.util.Result
import com.sns.homeconnect_v2.domain.repository.WifiRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress
import javax.inject.Inject
import kotlin.coroutines.resume

@RequiresApi(Build.VERSION_CODES.R)
class WifiRepositoryImpl @Inject constructor(
    private val context: Context,
    private val permissionManager: PermissionManager
) : WifiRepository {

    private val wifiManager: WifiManager
        get() = context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager

    private val connectivityManager: ConnectivityManager
        get() = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    override suspend fun scanWifiNetworks(): Result<List<ScanResult>> = withContext(Dispatchers.IO) {
        try {
            if (!permissionManager.getLocationWifiPermissions().all { permissionManager.isPermissionGranted(it) }) {
                return@withContext Result.Error("Location permissions required")
            }

            if (!wifiManager.isWifiEnabled) {
                return@withContext Result.Error("Wi-Fi is disabled. Please enable Wi-Fi in settings.")
            }

            wifiManager.startScan()

            val result = suspendCancellableCoroutine<Result<List<ScanResult>>> { continuation ->
                val executor = ContextCompat.getMainExecutor(context)

                val callback = object : WifiManager.ScanResultsCallback() {
                    override fun onScanResultsAvailable() {
                        val results = wifiManager.scanResults
                            .filter { it.SSID?.isNotEmpty() == true }
                            .distinctBy { it.SSID }

                        wifiManager.unregisterScanResultsCallback(this)
                        continuation.resume(Result.Success(results))
                    }
                }

                wifiManager.registerScanResultsCallback(executor, callback)

                Handler(Looper.getMainLooper()).postDelayed({
                    if (continuation.isActive) {
                        wifiManager.unregisterScanResultsCallback(callback)
                        continuation.resume(Result.Error("Scan timeout"))
                    }
                }, 5000)
            }

            return@withContext result
        } catch (e: SecurityException) {
            Result.Error("Permission denied: ${e.message}")
        } catch (e: Exception) {
            Result.Error("Failed to scan Wi-Fi: ${e.message}")
        }
    }

    override suspend fun connectToWifi(ssid: String, password: String): Result<Boolean> = withContext(Dispatchers.IO) {
        try {
            val wifiNetworkSpecifier = WifiNetworkSpecifier.Builder()
                .setSsid(ssid)
                .setWpa2Passphrase(password)
                .build()
            val networkRequest = NetworkRequest.Builder()
                .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
                .setNetworkSpecifier(wifiNetworkSpecifier)
                .build()
            var isConnected = false
            val callback = object : ConnectivityManager.NetworkCallback() {
                override fun onAvailable(network: Network) {
                    connectivityManager.bindProcessToNetwork(network)
                    isConnected = true
                }

                override fun onUnavailable() {
                    isConnected = false
                }
            }
            connectivityManager.requestNetwork(networkRequest, callback)
            Handler(Looper.getMainLooper()).postDelayed({
                connectivityManager.unregisterNetworkCallback(callback)
            }, 30000)
            Thread.sleep(5000)
            if (isConnected) Result.Success(true) else Result.Error("Failed to connect to $ssid")
        } catch (e: Exception) {
            Result.Error("Connection error: ${e.message}")
        }
    }

    override suspend fun sendWifiCredentials(
        espIp: String,
        port: Int,
        ssid: String,
        password: String
    ): Result<String> = withContext(Dispatchers.IO) {
        try {
            val socket = DatagramSocket()
            socket.soTimeout = 5000
            val address = InetAddress.getByName(espIp)
            val data = "SSID=$ssid;PASSWORD=$password;"
            val buffer = data.toByteArray()
            val packet = DatagramPacket(buffer, buffer.size, address, port)
            socket.send(packet)

            val responseBuffer = ByteArray(256)
            val responsePacket = DatagramPacket(responseBuffer, responseBuffer.size)
            socket.receive(responsePacket)
            val response = String(responsePacket.data, 0, responsePacket.length)
            socket.close()

            if (response.contains("SUCCESS")) {
                Result.Success(response)
            } else {
                Result.Error("ESP response: $response")
            }
        } catch (e: Exception) {
            Result.Error("UDP error: ${e.message}")
        }
    }
}
