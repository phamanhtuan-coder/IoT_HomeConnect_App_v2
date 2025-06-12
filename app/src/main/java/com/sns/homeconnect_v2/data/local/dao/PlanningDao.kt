package com.sns.homeconnect_v2.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sns.homeconnect_v2.data.local.entity.PlanningEntity

@Dao
interface PlanningDao {

    @Query("SELECT * FROM planning WHERE is_deleted = 0")
    suspend fun getAllActive(): List<PlanningEntity>

    @Query("SELECT * FROM planning WHERE planning_id = :id")
    suspend fun getById(id: String): PlanningEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(plans: List<PlanningEntity>)

    @Query("DELETE FROM planning")
    suspend fun clear()
}
