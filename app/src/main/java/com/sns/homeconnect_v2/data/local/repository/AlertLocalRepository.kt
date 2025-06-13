package com.sns.homeconnect_v2.data.local.repository

import com.sns.homeconnect_v2.data.local.dao.AlertDao
import com.sns.homeconnect_v2.data.local.entity.AlertEntity
import jakarta.inject.Inject

class AlertLocalRepository @Inject constructor(
    private val dao: AlertDao
) {
    suspend fun getAllAlerts() = dao.getAll()

    suspend fun saveAlerts(alerts: List<AlertEntity>) = dao.insertAll(alerts)

    suspend fun clearAlerts() = dao.clear()

    suspend fun getActiveAlerts() = dao.getActiveAlerts()

    suspend fun getAlertsByDeviceSerial(deviceSerial: String) = dao.getAlertsByDeviceSerial(deviceSerial)

    suspend fun getAlertsBySpaceId(spaceId: Int) = dao.getAlertsBySpaceId(spaceId)

    suspend fun getAlertsByStatus(status: String) = dao.getAlertsByStatus(status)

    suspend fun getAlertsInTimeRange(startTime: Long, endTime: Long) = dao.getAlertsInTimeRange(startTime, endTime)

    suspend fun getAlertsByType(alertTypeId: Int) = dao.getAlertsByType(alertTypeId)
}
