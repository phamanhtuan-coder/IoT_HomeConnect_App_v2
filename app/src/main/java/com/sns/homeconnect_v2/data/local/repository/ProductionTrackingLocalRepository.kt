package com.sns.homeconnect_v2.data.local.repository

import com.sns.homeconnect_v2.data.local.dao.ProductionTrackingDao
import com.sns.homeconnect_v2.data.local.entity.ProductionTrackingEntity
import jakarta.inject.Inject

class ProductionTrackingLocalRepository @Inject constructor(
    private val dao: ProductionTrackingDao
) {
    suspend fun getAllActiveTracking() = dao.getAllActive()

    suspend fun getTrackingByBatchId(batchId: String) = dao.getByBatchId(batchId)

    suspend fun saveTrackingList(list: List<ProductionTrackingEntity>) = dao.insertAll(list)

    suspend fun clearTracking() = dao.clear()
}
