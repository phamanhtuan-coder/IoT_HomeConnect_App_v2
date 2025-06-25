package com.sns.homeconnect_v2.domain.usecase.ticket

import com.sns.homeconnect_v2.data.remote.dto.request.CreateTicketRequest
import com.sns.homeconnect_v2.data.remote.dto.response.CreateTicketResponse
import com.sns.homeconnect_v2.domain.repository.TicketRepository
import retrofit2.HttpException
import javax.inject.Inject

class CreateTicketUseCase @Inject constructor(
    private val ticketRepository: TicketRepository
) {
    suspend operator fun invoke(request: CreateTicketRequest): Result<CreateTicketResponse> {
        return try {
            val response = ticketRepository.createTicket(request)
            Result.success(response)
        } catch (e: HttpException) {
            throw e // Ném HttpException để ViewModel xử lý mã HTTP
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}