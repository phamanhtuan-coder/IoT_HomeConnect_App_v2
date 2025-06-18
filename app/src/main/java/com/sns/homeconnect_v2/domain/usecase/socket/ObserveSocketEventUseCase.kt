package com.sns.homeconnect_v2.domain.usecase.socket

import com.sns.homeconnect_v2.domain.repository.SocketRepository
import org.json.JSONObject
import jakarta.inject.Inject

class ObserveSocketEventUseCase @Inject constructor(
    private val repository: SocketRepository
) {
    operator fun invoke(event: String, callback: (JSONObject) -> Unit) {
        repository.observeEvent(event, callback)
    }
}
