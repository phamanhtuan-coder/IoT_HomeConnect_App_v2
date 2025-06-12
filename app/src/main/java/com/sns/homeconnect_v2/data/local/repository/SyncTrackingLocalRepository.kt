package com.sns.homeconnect_v2.data.local.repository

import com.sns.homeconnect_v2.data.local.dao.SyncTrackingDao
import com.sns.homeconnect_v2.data.local.entity.SyncTrackingEntity
import jakarta.inject.Inject

class SyncTrackingLocalRepository @Inject constructor(
    private val dao: SyncTrackingDao
) {
    suspend fun getAllSyncRecords() = dao.getAll()

    suspend fun saveSyncRecords(records: List<SyncTrackingEntity>) = dao.insertAll(records)

    suspend fun clearSyncRecords() = dao.clear()

    suspend fun getSyncRecordsByAccountId(accountId: Int) = dao.getByAccountId(accountId)

    suspend fun getSyncRecordsByDeviceId(deviceId: Int) = dao.getByDeviceId(deviceId)
}
