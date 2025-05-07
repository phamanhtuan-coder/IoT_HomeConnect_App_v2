package com.sns.homeconnect_v2.domain.repository

import com.sns.homeconnect_v2.data.remote.dto.response.Alert
import com.sns.homeconnect_v2.data.remote.dto.response.AlertDetail
import com.sns.homeconnect_v2.data.remote.dto.response.AlertResponse


interface AlertRepository {

    suspend fun getAllByUser(): List<AlertResponse>

    suspend fun getAlertById(alertId: Int): AlertDetail

    suspend fun readNotification(alertId: Int): Alert

    suspend fun searchNotification(search: String): List<AlertResponse>
}