package com.sns.homeconnect_v2.domain.usecase.socket

import com.sns.homeconnect_v2.domain.repository.SocketRepository
import org.json.JSONObject
import jakarta.inject.Inject

class ObserveSocketEventUseCase @Inject constructor(
    private val repository: SocketRepository
) {
    operator fun invoke(onEvent: (String, JSONObject) -> Unit) {
        val events = listOf(
            "device_online",
            "sensorData",
            "alarmAlert",
            "deviceStatus",
            "buzzerStatus",
            "realtime_device_value"
        )

        events.forEach { event ->
            repository.observeEvent(event) { json ->
                onEvent(event, json)
            }
        }
    }
}
