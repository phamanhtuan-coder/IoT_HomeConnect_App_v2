package com.sns.homeconnect_v2.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sns.homeconnect_v2.data.local.entity.AlertTypeEntity

@Dao
interface AlertTypeDao {
    @Query("SELECT * FROM alert_types WHERE is_deleted = 0")
    suspend fun getActiveAlertTypes(): List<AlertTypeEntity>

    @Query("SELECT * FROM alert_types WHERE alert_type_id = :id")
    suspend fun getAlertTypeById(id: Int): AlertTypeEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(alertTypes: List<AlertTypeEntity>)

    @Query("DELETE FROM alert_types")
    suspend fun clear()
}
