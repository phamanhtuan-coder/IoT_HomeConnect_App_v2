package com.sns.homeconnect_v2.domain.usecase.notification

import android.util.Log
import com.sns.homeconnect_v2.data.remote.dto.response.AlertResponse
import com.sns.homeconnect_v2.domain.repository.AlertRepository
import javax.inject.Inject

class GetAllByUserUseCase @Inject constructor(
    private val repository: AlertRepository
) {
    suspend operator fun invoke(): Result<List<AlertResponse>> {
        return try {
            val response = repository.getAllByUser()
            Result.success(response)
        } catch (e: Exception) {
            Log.e("ListNotificationModel", "Error fetching alerts: ${e.message}")
            Result.failure(e)
        }
    }
}