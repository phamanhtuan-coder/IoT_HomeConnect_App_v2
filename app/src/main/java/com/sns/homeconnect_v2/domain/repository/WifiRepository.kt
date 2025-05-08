package com.sns.homeconnect_v2.domain.repository

import android.net.wifi.ScanResult
import com.sns.homeconnect_v2.core.util.Result

interface WifiRepository {
    suspend fun scanWifiNetworks(): Result<List<ScanResult>>
    suspend fun connectToWifi(ssid: String, password: String): Result<Boolean>
    suspend fun sendWifiCredentials(espIp: String, port: Int, ssid: String, password: String): Result<String>
}