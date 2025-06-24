package com.sns.homeconnect_v2.domain.usecase.ticket

import com.sns.homeconnect_v2.data.remote.dto.response.CancelTicketResponse
import com.sns.homeconnect_v2.domain.repository.TicketRepository
import javax.inject.Inject

class CancelTicketUsecase @Inject constructor(
private val ticketRepository: TicketRepository
) {
    suspend operator fun invoke(ticketId: String): Result<CancelTicketResponse> {
        return try {
            val response = ticketRepository.cancelTicket(ticketId)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}