package com.sns.homeconnect_v2.data.socket

import android.util.Log
import io.socket.client.IO
import io.socket.client.Socket
import org.json.JSONObject

object SocketManager {
    private lateinit var socket: Socket

    fun connect(deviceId: String, serial_number: String, accountId: String) {
        val options = IO.Options().apply {
            query = "deviceId=$serial_number&accountId=$accountId&isIoTDevice=false"
        }

        socket = IO.socket("https://iothomeconnectapiv2-production.up.railway.app", options)

        // 🟢 Đặt sớm hơn
        listenToEvents()

        socket.on(Socket.EVENT_CONNECT) {
            Log.d("Socket", "✅ Connected to server with serial=$serial_number")

            val payload = JSONObject().apply {
                put("deviceId", deviceId)
            }
            emit("start_real_time_device", payload)
            Log.d("Socket", "📤 Emit start_real_time_device with payload: $payload")
        }

        socket.on(Socket.EVENT_DISCONNECT) {
            Log.e("Socket", "❌ Disconnected from server")
        }

        socket.on(Socket.EVENT_CONNECT_ERROR) { args ->
            Log.e("Socket", "🚫 Connect error: ${args.firstOrNull()?.toString()}")
        }

        socket.connect()
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

