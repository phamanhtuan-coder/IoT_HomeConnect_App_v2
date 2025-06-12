package com.sns.homeconnect_v2.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sns.homeconnect_v2.data.local.entity.FirmwareUpdateHistoryEntity

@Dao
interface FirmwareUpdateHistoryDao {

    @Query("SELECT * FROM firmware_update_history WHERE is_deleted = 0")
    suspend fun getAllActive(): List<FirmwareUpdateHistoryEntity>

    @Query("SELECT * FROM firmware_update_history WHERE device_serial = :deviceSerial AND is_deleted = 0")
    suspend fun getByDeviceSerial(deviceSerial: String): List<FirmwareUpdateHistoryEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(history: List<FirmwareUpdateHistoryEntity>)

    @Query("DELETE FROM firmware_update_history")
    suspend fun clear()
}
