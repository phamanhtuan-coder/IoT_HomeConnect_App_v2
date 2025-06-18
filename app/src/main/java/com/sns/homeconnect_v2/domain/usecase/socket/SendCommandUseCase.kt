package com.sns.homeconnect_v2.domain.usecase.socket

import com.sns.homeconnect_v2.domain.repository.SocketRepository
import org.json.JSONObject
import jakarta.inject.Inject

class SendCommandUseCase @Inject constructor(
    private val repository: SocketRepository
) {
    operator fun invoke(payload: JSONObject) {
        repository.sendCommand(payload)
    }
}
