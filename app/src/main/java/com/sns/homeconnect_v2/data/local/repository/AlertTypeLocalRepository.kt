package com.sns.homeconnect_v2.data.local.repository

import com.sns.homeconnect_v2.data.local.dao.AlertTypeDao
import com.sns.homeconnect_v2.data.local.entity.AlertTypeEntity
import jakarta.inject.Inject

class AlertTypeLocalRepository @Inject constructor(
    private val dao: AlertTypeDao
) {
    suspend fun getActiveAlertTypes() = dao.getActiveAlertTypes()

    suspend fun getAlertTypeById(id: Int) = dao.getAlertTypeById(id)

    suspend fun saveAlertTypes(alertTypes: List<AlertTypeEntity>) = dao.insertAll(alertTypes)

    suspend fun clearAlertTypes() = dao.clear()
}
