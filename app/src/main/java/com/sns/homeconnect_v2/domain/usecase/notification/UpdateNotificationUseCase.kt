package com.sns.homeconnect_v2.domain.usecase.notification

import com.sns.homeconnect_v2.data.remote.dto.response.check
import com.sns.homeconnect_v2.domain.repository.NotificationRepository
import javax.inject.Inject

class UpdateNotificationUseCase @Inject constructor(
private val notificationRepository: NotificationRepository
) {
    suspend operator fun invoke(id: Int, isRead: check) {
        notificationRepository.updateNotificationStatus(id, isRead)
    }
}