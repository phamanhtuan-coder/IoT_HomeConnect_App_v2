package com.sns.homeconnect_v2.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sns.homeconnect_v2.data.local.entity.SyncTrackingEntity

@Dao
interface SyncTrackingDao {
    @Query("SELECT * FROM sync_tracking")
    suspend fun getAll(): List<SyncTrackingEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(syncRecords: List<SyncTrackingEntity>)

    @Query("DELETE FROM sync_tracking")
    suspend fun clear()

    @Query("SELECT * FROM sync_tracking WHERE account_id = :accountId")
    suspend fun getByAccountId(accountId: Int): List<SyncTrackingEntity>

    @Query("SELECT * FROM sync_tracking WHERE user_device_id = :deviceId")
    suspend fun getByDeviceId(deviceId: Int): List<SyncTrackingEntity>
}
