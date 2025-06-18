package com.sns.homeconnect_v2.domain.usecase.socket

import com.sns.homeconnect_v2.domain.repository.SocketRepository
import jakarta.inject.Inject

class ConnectSocketUseCase @Inject constructor(
    private val repository: SocketRepository
) {
    operator fun invoke(deviceId: String, serial_number: String, accountId: String) {
        repository.connect(deviceId, serial_number, accountId)
    }
}
