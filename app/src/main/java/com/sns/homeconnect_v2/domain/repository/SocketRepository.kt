package com.sns.homeconnect_v2.domain.repository

import org.json.JSONObject

interface SocketRepository {
    fun connect(deviceId: String, serial_number: String, accountId: String)
    fun disconnect()
    fun sendCommand(payload: JSONObject)
    fun observeEvent(event: String, callback: (JSONObject) -> Unit)
}
