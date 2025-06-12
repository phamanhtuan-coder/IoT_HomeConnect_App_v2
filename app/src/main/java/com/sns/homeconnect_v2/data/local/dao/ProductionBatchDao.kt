package com.sns.homeconnect_v2.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sns.homeconnect_v2.data.local.entity.ProductionBatchEntity

@Dao
interface ProductionBatchDao {

    @Query("SELECT * FROM production_batches WHERE is_deleted = 0")
    suspend fun getAllActive(): List<ProductionBatchEntity>

    @Query("SELECT * FROM production_batches WHERE production_batch_id = :id")
    suspend fun getByBatchId(id: String): ProductionBatchEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(batches: List<ProductionBatchEntity>)

    @Query("DELETE FROM production_batches")
    suspend fun clear()
}
