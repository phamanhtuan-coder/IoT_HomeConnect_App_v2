package com.sns.homeconnect_v2.presentation.viewmodel.profile

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sns.homeconnect_v2.data.remote.dto.request.UserRequest
import com.sns.homeconnect_v2.data.remote.dto.response.User
import com.sns.homeconnect_v2.data.remote.dto.response.UserResponse
import com.sns.homeconnect_v2.domain.usecase.auth.LogOutUseCase
import com.sns.homeconnect_v2.domain.usecase.profile.GetInfoProfileUseCase
import com.sns.homeconnect_v2.domain.usecase.profile.PutInfoProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

// Trạng thái đăng xuất
sealed class ProfileState {
    data object Idle : ProfileState()
    data object Loading : ProfileState()
    data class Success(val success: Boolean, val message: String) : ProfileState()
    data class Error(val success: Boolean, val message: String) : ProfileState()
}

// Trạng thái lấy profile
sealed class InfoProfileState {
    data object Idle : InfoProfileState()
    data object Loading : InfoProfileState()
    data class Success(val user: User) : InfoProfileState()
    data class Error(val error: String) : InfoProfileState()
}

// Trạng thái cập nhật profile
sealed class PutInfoProfileState {
    data object Idle : PutInfoProfileState()
    data object Loading : PutInfoProfileState()
    data class Success(val userResponse: UserResponse) : PutInfoProfileState()
    data class Error(val error: String) : PutInfoProfileState()
}

@HiltViewModel
class ProfileScreenViewModel @Inject constructor(
    private val logOutUseCase: LogOutUseCase,
    private val getInfoProfileUseCase: GetInfoProfileUseCase,
    private val putInfoProfileUseCase: PutInfoProfileUseCase,
) : ViewModel() {

    private val _logoutState = MutableStateFlow<ProfileState>(ProfileState.Idle)
    val logoutState = _logoutState.asStateFlow()

    private val _infoProfileState = MutableStateFlow<InfoProfileState>(InfoProfileState.Idle)
    val infoProfileState = _infoProfileState.asStateFlow()

    private val _putInfoProfileState = MutableStateFlow<PutInfoProfileState>(PutInfoProfileState.Idle)
    val putInfoProfileState = _putInfoProfileState.asStateFlow()

    // 🔓 Logout thiết bị hiện tại
    fun logout() {
        viewModelScope.launch {
            _logoutState.value = ProfileState.Loading
            logOutUseCase().fold(
                onSuccess = {
                    _logoutState.value = ProfileState.Success(true, "Đăng xuất thành công!")
                },
                onFailure = { e ->
                    _logoutState.value = ProfileState.Error(false, e.message ?: "Đăng xuất thất bại")
                }
            )
        }
    }

    // 🔓 Logout toàn bộ thiết bị
    fun logoutAllDevices() {
        viewModelScope.launch {
            _logoutState.value = ProfileState.Loading
            logOutUseCase.logoutAllDevices().fold(
                onSuccess = {
                    _logoutState.value = ProfileState.Success(true, "Đăng xuất toàn bộ thiết bị thành công.")
                },
                onFailure = { e ->
                    _logoutState.value = ProfileState.Error(false, e.message ?: "Đăng xuất toàn bộ thiết bị thất bại.")
                }
            )
        }
    }

    // 📄 Lấy thông tin người dùng
    fun getInfoProfile() {
        viewModelScope.launch {
            _infoProfileState.value = InfoProfileState.Loading
            getInfoProfileUseCase().fold(
                onSuccess = { response ->
                    Log.d("ProfileScreenViewModel", "Profile fetched: $response")
                    _infoProfileState.value = InfoProfileState.Success(response)
                },
                onFailure = { e ->
                    Log.e("ProfileScreenViewModel", "Error: ${e.message}")
                    _infoProfileState.value = InfoProfileState.Error(e.message ?: "Lỗi khi lấy thông tin profile!")
                }
            )
        }
    }

    // ✏️ Cập nhật thông tin người dùng
    fun putInfoProfile(userId: Int, user: UserRequest) {
        viewModelScope.launch {
            _putInfoProfileState.value = PutInfoProfileState.Loading
            putInfoProfileUseCase(userId, user).fold(
                onSuccess = { response ->
                    Log.d("ProfileScreenViewModel", "Update success: $response")
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
