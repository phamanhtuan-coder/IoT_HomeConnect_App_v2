package com.sns.homeconnect_v2.domain.usecase.ticket

import com.sns.homeconnect_v2.data.remote.dto.response.TicketDetailResponse
import com.sns.homeconnect_v2.domain.repository.TicketRepository
import javax.inject.Inject

class GetDetailTicketUseCase @Inject constructor(
private val ticketRepository: TicketRepository
) {
    suspend operator fun invoke(ticketId: String): Result<TicketDetailResponse> {
        return try {
            val response = ticketRepository.getTicketById(ticketId)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}