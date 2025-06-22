package com.sns.homeconnect_v2.presentation.viewmodel.ticket

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sns.homeconnect_v2.data.remote.dto.response.Ticket
import com.sns.homeconnect_v2.domain.repository.TicketRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class GetListTicketViewModel @Inject constructor(
    private val ticketRepository: TicketRepository
) : ViewModel() {

    private val _tickets = MutableStateFlow<List<Ticket>>(emptyList())
    val tickets: StateFlow<List<Ticket>> = _tickets.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    init {
        fetchTickets()
    }

    private fun fetchTickets() {
        viewModelScope.launch {
            try {
                val response = ticketRepository.getListTickets()
                if (response.code == 0) {
//                    _tickets.value = response.data!!.tickets
                    _tickets.value = response.data?.tickets?.map { ticket ->
                        ticket.copy(
                            status = ticket.status?.lowercase()?.takeIf { it in listOf("pending", "processed", "rejected") } ?: "pending",
                            createdAt = ticket.createdAt?.let { formatDate(it) } ?: "N/A",
                            userName = ticket.userName ?: "Unknown User",
                            ticketTypeName = ticket.ticketTypeName ?: "Unknown Type",
                            description = ticket.description ?: ""
                        )
                    } ?: emptyList()
                } else {
                    _errorMessage.value = response.message
                    _tickets.value = emptyList()
                }
            } catch (e: Exception) {
                _errorMessage.value = "Lỗi: ${e.message}"
                _tickets.value = emptyList()
            }
        }
    }

    private fun formatDate(dateString: String): String {
        return try {
            SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                .format(SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault()).parse(dateString)!!)
        } catch (e: Exception) {
            "N/A"
        }
    }
}