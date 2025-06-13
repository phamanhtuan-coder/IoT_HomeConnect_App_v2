package com.sns.homeconnect_v2.data.local.repository

import com.sns.homeconnect_v2.data.local.dao.ProductionBatchDao
import com.sns.homeconnect_v2.data.local.entity.ProductionBatchEntity
import jakarta.inject.Inject

class ProductionBatchLocalRepository @Inject constructor(
    private val dao: ProductionBatchDao
) {
    suspend fun getAllActiveBatches() = dao.getAllActive()

    suspend fun getBatchById(id: String) = dao.getByBatchId(id)

    suspend fun saveBatches(batches: List<ProductionBatchEntity>) = dao.insertAll(batches)

    suspend fun clearBatches() = dao.clear()
}
