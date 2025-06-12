package com.sns.homeconnect_v2.data.local.repository

import com.sns.homeconnect_v2.data.local.dao.SharedPermissionDao
import com.sns.homeconnect_v2.data.local.entity.SharedPermissionEntity
import jakarta.inject.Inject

class SharedPermissionLocalRepository @Inject constructor(
    private val dao: SharedPermissionDao
) {
    suspend fun getAllActivePermissions() = dao.getAllActive()

    suspend fun getPermissionsByDeviceSerial(deviceSerial: String) = dao.getByDeviceSerial(deviceSerial)

    suspend fun getPermissionsByUserId(userId: String) = dao.getByUserId(userId)

    suspend fun savePermissions(permissions: List<SharedPermissionEntity>) = dao.insertAll(permissions)

    suspend fun clearPermissions() = dao.clear()
}
