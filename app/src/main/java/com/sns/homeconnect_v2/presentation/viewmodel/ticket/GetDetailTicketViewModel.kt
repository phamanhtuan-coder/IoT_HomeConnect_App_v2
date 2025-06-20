package com.sns.homeconnect_v2.presentation.viewmodel.ticket

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sns.homeconnect_v2.data.remote.dto.response.TicketDetail
import com.sns.homeconnect_v2.domain.repository.TicketRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GetDetailTicketViewModel @Inject constructor(
private val ticketRepository: TicketRepository
) : ViewModel() {
    private val _ticketDetail = MutableStateFlow<TicketDetail?>(null)
    val ticketDetail: StateFlow<TicketDetail?> = _ticketDetail.asStateFlow()

    fun getTicketDetail(ticketId: String) {
        viewModelScope.launch {
            try {
                val response = ticketRepository.getTicketById(ticketId)
                if (response.code == 0) {
                    _ticketDetail.value = response.data?.ticket
                } else {
                    _ticketDetail.value = null
                }
            } catch (e: Exception) {
                _ticketDetail.value = null
            }
        }
    }
}