package com.sns.homeconnect_v2.domain.usecase.iot_device.connect

import android.net.wifi.ScanResult
import com.sns.homeconnect_v2.core.util.Result
import com.sns.homeconnect_v2.domain.repository.WifiRepository
import javax.inject.Inject

class ScanWifiNetworksUseCase @Inject constructor(
    internal val wifiRepository: WifiRepository
) {
    suspend operator fun invoke(): Result<List<ScanResult>> {
        return wifiRepository.scanWifiNetworks()
    }
}