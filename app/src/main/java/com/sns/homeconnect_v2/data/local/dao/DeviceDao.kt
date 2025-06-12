package com.sns.homeconnect_v2.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sns.homeconnect_v2.data.local.entity.DeviceEntity

@Dao
interface DeviceDao {
    @Query("SELECT * FROM devices")
    suspend fun getAll(): List<DeviceEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(devices: List<DeviceEntity>)

    @Query("DELETE FROM devices")
    suspend fun clear()

    @Query("SELECT * FROM devices WHERE is_deleted = 0")
    suspend fun getActiveDevices(): List<DeviceEntity>

    @Query("SELECT * FROM devices WHERE space_id = :spaceId AND is_deleted = 0")
    suspend fun getDevicesBySpaceId(spaceId: Int): List<DeviceEntity>

    @Query("SELECT * FROM devices WHERE group_id = :groupId AND is_deleted = 0")
    suspend fun getDevicesByGroupId(groupId: Int): List<DeviceEntity>

    @Query("SELECT * FROM devices WHERE device_id = :deviceId AND is_deleted = 0")
    suspend fun getDeviceById(deviceId: String): DeviceEntity?

    @Query("SELECT * FROM devices WHERE hub_id = :hubId AND is_deleted = 0")
    suspend fun getDevicesByHubId(hubId: String): List<DeviceEntity>

    @Query("SELECT * FROM devices WHERE account_id = :accountId AND is_deleted = 0")
    suspend fun getDevicesByAccountId(accountId: String): List<DeviceEntity>
}
