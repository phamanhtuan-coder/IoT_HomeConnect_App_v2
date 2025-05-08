package com.sns.homeconnect_v2.domain.usecase.iot_device.connect

import com.sns.homeconnect_v2.core.util.Result
import com.sns.homeconnect_v2.domain.repository.WifiRepository
import javax.inject.Inject

class SendWifiCredentialsUseCase @Inject constructor(
    private val wifiRepository: WifiRepository
) {
    suspend operator fun invoke(espIp: String, port: Int, ssid: String, password: String): Result<String> {
        return wifiRepository.sendWifiCredentials(espIp, port, ssid, password)
    }
}