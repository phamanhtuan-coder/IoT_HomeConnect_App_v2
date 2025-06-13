package com.sns.homeconnect_v2.data.local.repository

import com.sns.homeconnect_v2.data.local.dao.DeviceDao
import com.sns.homeconnect_v2.data.local.entity.DeviceEntity
import jakarta.inject.Inject

class DeviceLocalRepository @Inject constructor(
    private val dao: DeviceDao
) {
    suspend fun getAllDevices() = dao.getAll()

    suspend fun saveDevices(devices: List<DeviceEntity>) = dao.insertAll(devices)

    suspend fun clearDevices() = dao.clear()

    suspend fun getActiveDevices() = dao.getActiveDevices()

    suspend fun getDevicesBySpaceId(spaceId: Int) = dao.getDevicesBySpaceId(spaceId)

    suspend fun getDevicesByGroupId(groupId: Int) = dao.getDevicesByGroupId(groupId)

    suspend fun getDeviceById(deviceId: String) = dao.getDeviceById(deviceId)

    suspend fun getDevicesByHubId(hubId: String) = dao.getDevicesByHubId(hubId)

    suspend fun getDevicesByAccountId(accountId: String) = dao.getDevicesByAccountId(accountId)
}
