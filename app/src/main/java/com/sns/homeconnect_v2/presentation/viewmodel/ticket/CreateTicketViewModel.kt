package com.sns.homeconnect_v2.presentation.viewmodel.ticket

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sns.homeconnect_v2.data.remote.dto.request.CreateTicketRequest
import com.sns.homeconnect_v2.data.remote.dto.response.CreateTicketResponse
import com.sns.homeconnect_v2.domain.usecase.ticket.CreateTicketUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import javax.inject.Inject
import android.util.Log

@HiltViewModel
class CreateTicketViewModel @Inject constructor(
    private val createTicketUseCase: CreateTicketUseCase
) : ViewModel() {

    private val _createResult = MutableStateFlow<Result<CreateTicketResponse>?>(null)
    val createResult: StateFlow<Result<CreateTicketResponse>?> = _createResult.asStateFlow()

    fun createTicket(request: CreateTicketRequest) {
        viewModelScope.launch {
            try {
                val result = createTicketUseCase(request)
                _createResult.value = result
            } catch (e: HttpException) {
                Log.e("CreateTicketViewModel", "HTTP error: ${e.code()}, message: ${e.message}, body: ${e.response()?.errorBody()?.string()}")
                val errorMessage = when (e.code()) {
                    404 -> "ticket_type_id: Không tìm thấy người nhận thiết bị được yêu cầu"
                    else -> "Lỗi ${e.code()}: ${e.message ?: "Không xác định"}"
                }
                _createResult.value = Result.failure(Exception(errorMessage))
            } catch (e: Exception) {
                Log.e("CreateTicketViewModel", "Unexpected error: ${e.message}")
                _createResult.value = Result.failure(Exception("Lỗi: ${e.message ?: "Không xác định"}"))
            }
        }
    }

    fun resetResult() {
        _createResult.value = null
    }
}