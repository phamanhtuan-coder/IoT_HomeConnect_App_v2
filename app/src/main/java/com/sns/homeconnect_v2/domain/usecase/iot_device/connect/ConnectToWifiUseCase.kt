package com.sns.homeconnect_v2.domain.usecase.iot_device.connect

import com.sns.homeconnect_v2.core.util.Result
import com.sns.homeconnect_v2.domain.repository.WifiRepository
import javax.inject.Inject

class ConnectToWifiUseCase @Inject constructor(
    private val wifiRepository: WifiRepository
) {
    suspend operator fun invoke(ssid: String, password: String): Result<Boolean> {
        return wifiRepository.connectToWifi(ssid, password)
    }
}