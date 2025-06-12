package com.sns.homeconnect_v2.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sns.homeconnect_v2.data.local.entity.HourlyValueEntity

@Dao
interface HourlyValueDao {
    @Query("SELECT * FROM hourly_values")
    suspend fun getAll(): List<HourlyValueEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(values: List<HourlyValueEntity>)

    @Query("DELETE FROM hourly_values")
    suspend fun clear()

    @Query("SELECT * FROM hourly_values WHERE device_serial = :deviceSerial AND is_deleted = 0")
    suspend fun getValuesByDeviceSerial(deviceSerial: String): List<HourlyValueEntity>

    @Query("SELECT * FROM hourly_values WHERE space_id = :spaceId AND is_deleted = 0")
    suspend fun getValuesBySpaceId(spaceId: Int): List<HourlyValueEntity>

    @Query("SELECT * FROM hourly_values WHERE hour_timestamp BETWEEN :startTime AND :endTime AND device_serial = :deviceSerial AND is_deleted = 0")
    suspend fun getValuesInTimeRange(deviceSerial: String, startTime: Long, endTime: Long): List<HourlyValueEntity>
}
