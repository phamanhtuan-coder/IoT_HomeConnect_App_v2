package com.sns.homeconnect_v2.data.local.repository

import com.sns.homeconnect_v2.data.local.dao.FirmwareUpdateHistoryDao
import com.sns.homeconnect_v2.data.local.entity.FirmwareUpdateHistoryEntity
import jakarta.inject.Inject

class FirmwareUpdateHistoryLocalRepository @Inject constructor(
    private val dao: FirmwareUpdateHistoryDao
) {
    suspend fun getAllActiveUpdateHistories() = dao.getAllActive()

    suspend fun getUpdateHistoriesByDevice(deviceSerial: String) = dao.getByDeviceSerial(deviceSerial)

    suspend fun saveUpdateHistories(histories: List<FirmwareUpdateHistoryEntity>) = dao.insertAll(histories)

    suspend fun clearUpdateHistories() = dao.clear()
}
