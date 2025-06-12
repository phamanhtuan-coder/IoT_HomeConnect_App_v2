package com.sns.homeconnect_v2.data.local.repository

import com.sns.homeconnect_v2.data.local.dao.OwnershipHistoryDao
import com.sns.homeconnect_v2.data.local.entity.OwnershipHistoryEntity
import jakarta.inject.Inject

class OwnershipHistoryLocalRepository @Inject constructor(
    private val dao: OwnershipHistoryDao
) {
    suspend fun getAllActiveHistories() = dao.getAllActive()

    suspend fun getHistoriesByDeviceSerial(deviceSerial: String) = dao.getByDeviceSerial(deviceSerial)

    suspend fun saveHistories(histories: List<OwnershipHistoryEntity>) = dao.insertAll(histories)

    suspend fun clearHistories() = dao.clear()
}
