package com.sns.homeconnect_v2.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sns.homeconnect_v2.data.local.entity.OwnershipHistoryEntity

@Dao
interface OwnershipHistoryDao {

    @Query("SELECT * FROM ownership_history WHERE is_deleted = 0")
    suspend fun getAllActive(): List<OwnershipHistoryEntity>

    @Query("SELECT * FROM ownership_history WHERE device_serial = :deviceSerial AND is_deleted = 0")
    suspend fun getByDeviceSerial(deviceSerial: String): List<OwnershipHistoryEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(history: List<OwnershipHistoryEntity>)

    @Query("DELETE FROM ownership_history")
    suspend fun clear()
}
