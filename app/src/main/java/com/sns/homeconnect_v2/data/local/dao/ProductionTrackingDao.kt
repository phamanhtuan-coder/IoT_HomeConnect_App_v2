package com.sns.homeconnect_v2.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sns.homeconnect_v2.data.local.entity.ProductionTrackingEntity

@Dao
interface ProductionTrackingDao {

    @Query("SELECT * FROM production_tracking WHERE is_deleted = 0")
    suspend fun getAllActive(): List<ProductionTrackingEntity>

    @Query("SELECT * FROM production_tracking WHERE production_batch_id = :batchId AND is_deleted = 0")
    suspend fun getByBatchId(batchId: String): List<ProductionTrackingEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(trackingList: List<ProductionTrackingEntity>)

    @Query("DELETE FROM production_tracking")
    suspend fun clear()
}
