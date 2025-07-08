package com.sns.homeconnect_v2.data.socket

import android.util.Log
import io.socket.client.IO
import io.socket.client.Socket
import org.json.JSONObject

object SocketManager {
    private const val BASE_SOCKET_URL = "https://iothomeconnectapiv2-production.up.railway.app"

    private lateinit var socket: Socket

    fun connect(serial: String, accountId: String) {
        val opts = IO.Options().apply {
            query       = "serialNumber=$serial&accountId=$accountId"
            transports  = arrayOf("websocket")          // tránh fallback polling
            reconnectionAttempts = 3
        }

        /* ⭐️  Kết nối đúng namespace “/client” */
        socket = IO.socket("$BASE_SOCKET_URL/client", opts)

        Log.d("Socket", "$socket")

        /* đăng ký lắng nghe trước khi gọi connect() */
        attachCoreEvents(serial)

        socket.connect()
    }

    /** Các event cốt lõi */
    private fun attachCoreEvents(serial: String) {
        socket.on(Socket.EVENT_CONNECT) {
            Log.d("Socket", "✅ CONNECTED to /client, serial=$serial")

            /* yêu cầu server bật realtime */
            emit("start_real_time_device", JSONObject().put("serialNumber", serial))
        }

        socket.on(Socket.EVENT_CONNECT_ERROR) { e ->
            Log.e("Socket", "🚫 CONNECT ERROR: ${e.firstOrNull()}")
        }

        socket.on(Socket.EVENT_DISCONNECT)   { Log.w("Socket", "❌ DISCONNECTED") }
    }

    fun disconnect() {
        if (::socket.isInitialized) {
            socket.disconnect()
            Log.d("Socket", "❌ Manually disconnected")
        }
    }

    fun emit(event: String, data: JSONObject) {
        if (::socket.isInitialized && socket.connected()) {
            Log.d("Socket", "📤 Emit event: $event with data: $data")
            socket.emit(event, data)
        } else {
            Log.w("Socket", "⚠️ Emit failed: socket not connected for event: $event")
        }
    }

    fun on(event: String, callback: (JSONObject) -> Unit) {
        Log.d("Socket", "👂 Registering listener for event: $event") // ➕ Thêm dòng này

        if (::socket.isInitialized) {
            socket.on(event) { args ->
                val json = args[0] as? JSONObject
                if (json != null) {
                    Log.d("🔥 Socket Event", "📥 [$event] $json")
                    callback(json)
                } else {
                    Log.w("Socket", "⚠️ Event $event received invalid data: ${args.joinToString()}")
                }
            }
        }
    }


    fun listenToEvents() {
        on("device_online") {
            Log.d("Socket", "📡 device_online: $it")
        }

        on("realtime_device_value") {
            Log.d("Socket", "🔁 realtime_device_value: $it")
        }

        on("alarmAlert") {
            Log.d("Socket", "🚨 alarmAlert: $it")
        }

        on("sensorData") {
            Log.d("Socket", "🌡 sensorData: $it")
        }

        on("deviceStatus") {
            Log.d("Socket", "📦 deviceStatus: $it")
        }
    }
}

