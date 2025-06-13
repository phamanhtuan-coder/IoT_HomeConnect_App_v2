package com.sns.homeconnect_v2.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sns.homeconnect_v2.data.local.entity.ComponentEntity

@Dao
interface ComponentDao {

    @Query("SELECT * FROM components WHERE is_deleted = 0")
    suspend fun getAllActive(): List<ComponentEntity>

    @Query("SELECT * FROM components WHERE component_id = :id")
    suspend fun getById(id: String): ComponentEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(components: List<ComponentEntity>)

    @Query("DELETE FROM components")
    suspend fun clear()
}
