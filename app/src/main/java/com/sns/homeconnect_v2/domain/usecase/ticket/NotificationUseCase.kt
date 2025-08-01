package com.sns.homeconnect_v2.domain.usecase.ticket

import com.sns.homeconnect_v2.data.remote.dto.response.Notification
import com.sns.homeconnect_v2.domain.repository.NotificationRepository
import javax.inject.Inject

class NotificationUseCase @Inject constructor(
    private val notificationRepository: NotificationRepository
) {
    suspend operator fun invoke(): List<Notification>{
        return notificationRepository.getUserNotifications()
    }
}