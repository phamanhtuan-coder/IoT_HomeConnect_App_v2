package com.sns.homeconnect_v2.presentation.viewmodel.profile

import androidx.lifecycle.viewModelScope
import androidx.lifecycle.ViewModel
import com.sns.homeconnect_v2.data.remote.dto.request.ChangePasswordRequest
import com.sns.homeconnect_v2.domain.usecase.profile.UpdatePasswordUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


sealed class UpdatePasswordState {
    data object Idle : UpdatePasswordState()               // Chưa làm gì
    data object Loading : UpdatePasswordState()            // Đang loading
    data class Success(val message: String) : UpdatePasswordState()
    data class Error(val error: String) : UpdatePasswordState()
}

@HiltViewModel
class UpdatePasswordViewModel @Inject constructor (
    private val updatePasswordUseCase: UpdatePasswordUseCase
) : ViewModel() {
    // StateFlow cho UI lắng nghe
    private val _updatePasswordState =
        MutableStateFlow<UpdatePasswordState>(UpdatePasswordState.Idle)
    val updatePasswordState = _updatePasswordState.asStateFlow()

    // Hàm login
    fun updatePassword(userId: Int, changePasswordRequest: ChangePasswordRequest) {
        // Reset state
        _updatePasswordState.value = UpdatePasswordState.Loading

        viewModelScope.launch {
            updatePasswordUseCase(userId, changePasswordRequest).fold(
                onSuccess = { response ->
                    _updatePasswordState.value = UpdatePasswordState.Success(response.message)
                },
                onFailure = { e ->
                    _updatePasswordState.value = UpdatePasswordState.Error(e.message ?: "Failed to update password")
                }
            )

        }
    }
}