package com.sns.homeconnect_v2.data.repository

import com.sns.homeconnect_v2.data.AuthManager
import com.sns.homeconnect_v2.data.remote.api.ApiService
import com.sns.homeconnect_v2.data.remote.dto.request.CreateTicketRequest
import com.sns.homeconnect_v2.data.remote.dto.response.CancelTicketResponse
import com.sns.homeconnect_v2.data.remote.dto.response.CreateTicketResponse
import com.sns.homeconnect_v2.data.remote.dto.response.TicketDetailResponse
import com.sns.homeconnect_v2.data.remote.dto.response.TicketResponse
import com.sns.homeconnect_v2.domain.repository.TicketRepository
import javax.inject.Inject

class TicketRepositoryImpl @Inject constructor(
    private val ticketApi: ApiService,
    private val authManager: AuthManager
) : TicketRepository {

    override suspend fun getListTickets(): TicketResponse {
        val token = authManager.getJwtToken()
        return ticketApi.getUserTickets(token = "Bearer $token")
    }

    override suspend fun getTicketById(ticketId: String): TicketDetailResponse {
        val token = authManager.getJwtToken()
        return ticketApi.getTicketDetail(ticketId, token = "Bearer $token")
    }

    override suspend fun createTicket(request: CreateTicketRequest): CreateTicketResponse {
        val token = authManager.getJwtToken()
        return ticketApi.createTicket(request, token = "Bearer $token")
    }

    override suspend fun cancelTicket(ticketId: String): CancelTicketResponse {
        val token = authManager.getJwtToken()
        return ticketApi.cancelTicket(ticketId, token = "Bearer $token")
    }
}