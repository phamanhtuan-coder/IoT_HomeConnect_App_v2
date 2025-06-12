package com.sns.homeconnect_v2.data.local.repository

import com.sns.homeconnect_v2.data.local.dao.ShareRequestDao
import com.sns.homeconnect_v2.data.local.entity.ShareRequestEntity
import jakarta.inject.Inject

class ShareRequestLocalRepository @Inject constructor(
    private val dao: ShareRequestDao
) {
    suspend fun getAllActiveRequests() = dao.getAllActive()

    suspend fun getRequestsByToUserId(userId: String) = dao.getByToUserId(userId)

    suspend fun getRequestsByDeviceSerial(deviceSerial: String) = dao.getByDeviceSerial(deviceSerial)

    suspend fun saveRequests(requests: List<ShareRequestEntity>) = dao.insertAll(requests)

    suspend fun clearRequests() = dao.clear()
}
