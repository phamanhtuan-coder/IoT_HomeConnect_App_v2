package com.sns.homeconnect_v2.data.repository


import com.sns.homeconnect_v2.data.socket.SocketManager
import com.sns.homeconnect_v2.domain.repository.SocketRepository
import org.json.JSONObject
import javax.inject.Inject

class SocketRepositoryImpl @Inject constructor() : SocketRepository {
    override fun connect(deviceId: String, serial_number: String, accountId: String) {
        SocketManager.connect(serial_number, accountId)
    }

    override fun disconnect() {
        SocketManager.disconnect()
    }

    override fun sendCommand(payload: JSONObject) {
        SocketManager.emit("command", payload)
    }

    override fun observeEvent(event: String, callback: (JSONObject) -> Unit) {
        SocketManager.on(event, callback)
    }
}
