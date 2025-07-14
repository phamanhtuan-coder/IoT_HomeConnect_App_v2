package com.sns.homeconnect_v2.domain.usecase.iot_device

import com.sns.homeconnect_v2.domain.repository.SharedRepository
import javax.inject.Inject

class ApproveSharePermissionUseCase @Inject constructor(
    private val sharedRepository: SharedRepository
) {
    suspend operator fun invoke(ticketId: String): Result<Unit> {
        return try {
            sharedRepository.approveSharePermission(ticketId)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
