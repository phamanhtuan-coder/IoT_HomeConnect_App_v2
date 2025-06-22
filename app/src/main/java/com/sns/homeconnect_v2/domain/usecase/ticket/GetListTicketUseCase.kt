package com.sns.homeconnect_v2.domain.usecase.ticket

import com.sns.homeconnect_v2.data.remote.dto.response.TicketResponse
import com.sns.homeconnect_v2.domain.repository.TicketRepository
import javax.inject.Inject

class GetListTicketUseCase @Inject constructor(
private val ticketRepository: TicketRepository
) {
    suspend operator fun invoke(): TicketResponse{
        return ticketRepository.getListTickets()
    }
}