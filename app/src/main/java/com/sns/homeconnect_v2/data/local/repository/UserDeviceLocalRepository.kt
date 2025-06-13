package com.sns.homeconnect_v2.data.local.repository

import com.sns.homeconnect_v2.data.local.dao.UserDeviceDao
import com.sns.homeconnect_v2.data.local.entity.UserDeviceEntity
import jakarta.inject.Inject

class UserDeviceLocalRepository @Inject constructor(
    private val dao: UserDeviceDao
) {
    suspend fun getUserDevices() = dao.getAll()

    suspend fun saveUserDevices(devices: List<UserDeviceEntity>) = dao.insertAll(devices)

    suspend fun clearUserDevices() = dao.clear()
}
