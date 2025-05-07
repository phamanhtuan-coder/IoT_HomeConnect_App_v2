package com.sns.homeconnect_v2.presentation.viewmodel.profile

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sns.homeconnect_v2.data.remote.dto.request.UserRequest
import com.sns.homeconnect_v2.data.remote.dto.response.User
import com.sns.homeconnect_v2.data.remote.dto.response.UserResponse
import com.sns.homeconnect_v2.domain.usecase.auth.LogOutUseCase
import com.sns.homeconnect_v2.domain.usecase.iot_device.GetInfoProfileUseCase
import com.sns.homeconnect_v2.domain.usecase.profile.PutInfoProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class ProfileState {
    data object  Idle : ProfileState()
    data object   Loading : ProfileState()
    data class Success(val success: Boolean, val message: String) : ProfileState()
    data class Error(val success: Boolean, val message: String) : ProfileState()
}

sealed class InfoProfileState{
    data object  Idle :  InfoProfileState()              // Chưa làm gì
    data object  Loading :  InfoProfileState()            // Đang loading
    data class Success(val user: User) : InfoProfileState()
    data class Error(val error: String) : InfoProfileState()
}

sealed class PutInfoProfileState{
    data object  Idle :  PutInfoProfileState()              // Chưa làm gì
    data object  Loading :  PutInfoProfileState()            // Đang loading
    data class Success(val userResponse: UserResponse) : PutInfoProfileState()
    data class Error(val error: String) : PutInfoProfileState()
}

@HiltViewModel
class ProfileScreenViewModel  @Inject constructor(
    private val logOutUseCase: LogOutUseCase,
    private val getInfoProfileUseCase: GetInfoProfileUseCase,
    private val putInfoProfileUseCase: PutInfoProfileUseCase,
) : ViewModel() {

    private val _logoutState = MutableStateFlow<ProfileState>(ProfileState.Idle)
    val logoutState = _logoutState.asStateFlow()

    // Handle logout and navigation
    fun logout() {
        viewModelScope.launch {
            _logoutState.value = ProfileState.Loading
            logOutUseCase().fold(
                onSuccess = {
                    _logoutState.value = ProfileState.Success(true, "Logout successful")
                },
                onFailure = { e ->
                    _logoutState.value = ProfileState.Error(false, e.message ?: "Logout failed")
                }
            )
        }
    }

    private val _infoProfileState = MutableStateFlow<InfoProfileState>(InfoProfileState.Idle)
    val infoProfileState = _infoProfileState.asStateFlow()

    /**
     * Lấy thông tin profile
     */
    fun getInfoProfile() {
        viewModelScope.launch {
            getInfoProfileUseCase().fold(
                onSuccess = { response ->
                    Log.d("ProfileScreenViewModel", "Success: $response")
                    _infoProfileState.value = InfoProfileState.Success(response)
                },
                onFailure = { e ->
                    Log.e("ProfileScreenViewModel", "Error: ${e.message}")
                    _infoProfileState.value = InfoProfileState.Error(e.message ?: "Lỗi khi lấy thông tin profile!")
                }
            )
        }
    }

    private val _putInfoProfileState = MutableStateFlow<PutInfoProfileState>(PutInfoProfileState.Idle)
    val putInfoProfileState = _putInfoProfileState.asStateFlow()

    fun putInfoProfile(userId: Int, user: UserRequest) {
        viewModelScope.launch {
           putInfoProfileUseCase(userId, user).fold(
                onSuccess = { response ->
                    Log.d("ProfileScreenViewModel", "Success: $response")
                    _putInfoProfileState.value = PutInfoProfileState.Success(response)
                },
                onFailure = { e ->
                    Log.e("ProfileScreenViewModel", "Error: ${e.message}")
                    _putInfoProfileState.value = PutInfoProfileState.Error(e.message ?: "Lỗi khi cập nhật thông tin profile!")
                }
            )
        }
    }
}