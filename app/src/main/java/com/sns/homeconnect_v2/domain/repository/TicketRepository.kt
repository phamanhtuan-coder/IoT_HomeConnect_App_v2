package com.sns.homeconnect_v2.domain.repository

import com.sns.homeconnect_v2.data.remote.dto.response.TicketDetailResponse
import com.sns.homeconnect_v2.data.remote.dto.response.TicketResponse

interface TicketRepository {
    suspend fun getListTickets():TicketResponse
    suspend fun getTicketById(ticketId: String): TicketDetailResponse
}