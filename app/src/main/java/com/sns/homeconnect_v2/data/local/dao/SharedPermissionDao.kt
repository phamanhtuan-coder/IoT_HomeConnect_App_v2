package com.sns.homeconnect_v2.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sns.homeconnect_v2.data.local.entity.SharedPermissionEntity

@Dao
interface SharedPermissionDao {

    @Query("SELECT * FROM shared_permissions WHERE is_deleted = 0")
    suspend fun getAllActive(): List<SharedPermissionEntity>

    @Query("SELECT * FROM shared_permissions WHERE device_serial = :deviceSerial AND is_deleted = 0")
    suspend fun getByDeviceSerial(deviceSerial: String): List<SharedPermissionEntity>

    @Query("SELECT * FROM shared_permissions WHERE shared_with_user_id = :userId AND is_deleted = 0")
    suspend fun getByUserId(userId: String): List<SharedPermissionEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(permissions: List<SharedPermissionEntity>)

    @Query("DELETE FROM shared_permissions")
    suspend fun clear()
}
