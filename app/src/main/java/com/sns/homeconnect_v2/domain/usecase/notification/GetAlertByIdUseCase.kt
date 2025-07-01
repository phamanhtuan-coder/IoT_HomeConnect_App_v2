package com.sns.homeconnect_v2.domain.usecase.notification

import android.util.Log
import com.sns.homeconnect_v2.data.remote.dto.response.AlertDetail
import com.sns.homeconnect_v2.data.remote.dto.response.Notification
import com.sns.homeconnect_v2.domain.repository.AlertRepository
import com.sns.homeconnect_v2.domain.repository.NotificationRepository
import javax.inject.Inject

class GetAlertByIdUseCase @Inject constructor(
    private val repository: NotificationRepository
) {
    suspend operator fun invoke(id: Int): Notification {
        return repository.getUserNotificationById(id)
    }
}