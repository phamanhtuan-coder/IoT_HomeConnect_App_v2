package com.sns.homeconnect_v2.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sns.homeconnect_v2.data.local.entity.AlertEntity

@Dao
interface AlertDao {
    @Query("SELECT * FROM alerts")
    suspend fun getAll(): List<AlertEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(alerts: List<AlertEntity>)

    @Query("DELETE FROM alerts")
    suspend fun clear()

    @Query("SELECT * FROM alerts WHERE is_deleted = 0 ORDER BY timestamp DESC")
    suspend fun getActiveAlerts(): List<AlertEntity>

    @Query("SELECT * FROM alerts WHERE device_serial = :deviceSerial AND is_deleted = 0")
    suspend fun getAlertsByDeviceSerial(deviceSerial: String): List<AlertEntity>

    @Query("SELECT * FROM alerts WHERE space_id = :spaceId AND is_deleted = 0")
    suspend fun getAlertsBySpaceId(spaceId: Int): List<AlertEntity>

    @Query("SELECT * FROM alerts WHERE status = :status AND is_deleted = 0")
    suspend fun getAlertsByStatus(status: String): List<AlertEntity>

    @Query("SELECT * FROM alerts WHERE timestamp BETWEEN :startTime AND :endTime AND is_deleted = 0")
    suspend fun getAlertsInTimeRange(startTime: Long, endTime: Long): List<AlertEntity>

    @Query("SELECT * FROM alerts WHERE alert_type_id = :alertTypeId AND is_deleted = 0")
    suspend fun getAlertsByType(alertTypeId: Int): List<AlertEntity>
}
