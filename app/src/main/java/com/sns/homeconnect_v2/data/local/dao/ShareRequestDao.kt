package com.sns.homeconnect_v2.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sns.homeconnect_v2.data.local.entity.ShareRequestEntity

@Dao
interface ShareRequestDao {

    @Query("SELECT * FROM share_requests WHERE is_deleted = 0")
    suspend fun getAllActive(): List<ShareRequestEntity>

    @Query("SELECT * FROM share_requests WHERE to_user_id = :userId AND is_deleted = 0")
    suspend fun getByToUserId(userId: String): List<ShareRequestEntity>

    @Query("SELECT * FROM share_requests WHERE device_serial = :deviceSerial AND is_deleted = 0")
    suspend fun getByDeviceSerial(deviceSerial: String): List<ShareRequestEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(requests: List<ShareRequestEntity>)

    @Query("DELETE FROM share_requests")
    suspend fun clear()
}
