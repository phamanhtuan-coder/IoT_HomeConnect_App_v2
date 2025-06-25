package com.sns.homeconnect_v2.presentation.viewmodel.ticket

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sns.homeconnect_v2.domain.usecase.ticket.CancelTicketUsecase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CancelTicketViewModel @Inject constructor(
    private val cancelTicketUseCase: CancelTicketUsecase
): ViewModel()
{
    fun cancelTicket(ticketId: String) {
        viewModelScope.launch {
            try {
                cancelTicketUseCase(ticketId)
            } catch (e: Exception) {
                // Handle the exception, e.g., log it or show a message to the user
                e.printStackTrace()
            }
        }
    }
}