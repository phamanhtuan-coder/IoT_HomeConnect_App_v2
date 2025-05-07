package com.sns.homeconnect_v2.domain.usecase.notification

import android.util.Log
import com.sns.homeconnect_v2.data.remote.dto.response.AlertDetail
import com.sns.homeconnect_v2.domain.repository.AlertRepository
import javax.inject.Inject

class GetAlertByIdUseCase @Inject constructor(
    private val repository: AlertRepository
) {
    suspend operator fun invoke(alertId: Int): Result<AlertDetail> {
        return  try {
            val response = repository.getAlertById(alertId)
            Log.d("NotificationModel", "Alerts: $response")
           Result.success(response)
        } catch (e: Exception) {
            Log.e("NotificationModel", "Error fetching alerts: ${e.message}")
           Result.failure(e)
        }
    }
}