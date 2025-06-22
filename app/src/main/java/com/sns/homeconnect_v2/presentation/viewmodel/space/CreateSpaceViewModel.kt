package com.sns.homeconnect_v2.presentation.viewmodel.space

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sns.homeconnect_v2.data.remote.dto.request.CreateSpaceRequest
import com.sns.homeconnect_v2.domain.usecase.space.CreateSpaceUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import javax.inject.Inject

@HiltViewModel
class CreateSpaceViewModel @Inject constructor(
    private val createSpaceUseCase: CreateSpaceUseCase
) : ViewModel() {
    // State cho các trường nhập liệu
    private val _spaceName = MutableStateFlow("")
    val spaceName: StateFlow<String> = _spaceName.asStateFlow()

    private val _iconColor = MutableStateFlow("#FF0000") // Mặc định đỏ
    val iconColor: StateFlow<String> = _iconColor.asStateFlow()

    private val _iconName = MutableStateFlow("home") // Mặc định icon home
    val iconName: StateFlow<String> = _iconName.asStateFlow()

    private val _spaceDescription = MutableStateFlow("")
    val spaceDescription: StateFlow<String> = _spaceDescription.asStateFlow()

    // State cho trạng thái loading
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    // State cho lỗi
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    // State cho trạng thái thành công
    private val _isSuccess = MutableStateFlow(false)
    val isSuccess: StateFlow<Boolean> = _isSuccess.asStateFlow()

    // Cập nhật các trường nhập liệu
    fun updateSpaceName(name: String) {
        _spaceName.value = name
    }

    fun updateIconColor(color: String) {
        _iconColor.value = color
    }

    fun updateIconName(name: String) {
        _iconName.value = name
    }

    fun updateSpaceDescription(description: String) {
        _spaceDescription.value = description
    }

    // Tạo không gian mới
    fun createSpace(houseId: Int) {
        if (_spaceName.value.isBlank() || _iconColor.value.isBlank() || _iconName.value.isBlank()) {
            _error.value = "Vui lòng nhập đầy đủ tên, màu sắc và tên icon"
            return
        }

        viewModelScope.launch {
            _isLoading.value = true
            try {
                val request = CreateSpaceRequest(
                    houseId = houseId,
                    space_name = _spaceName.value,
                    icon_color = _iconColor.value,
                    icon_name = _iconName.value,
                    space_description = _spaceDescription.value.takeIf { it.isNotBlank() }
                )
                Log.d("CreateSpaceViewModel", "Request: $request")
                val result = createSpaceUseCase(request)
                Log.d("CreateSpaceViewModel", "Result: $result")
                result.onSuccess {
                    _isSuccess.value = true
                    _error.value = null
                    Log.d("CreateSpaceViewModel", "Success: Space created")
                }.onFailure { e ->
                    if (e is HttpException) {
                        val errorBody = e.response()?.errorBody()?.string()
                        Log.e("CreateSpaceViewModel", "HTTP ${e.code()}: $errorBody")
                        _error.value = errorBody ?: "Không được phép: Vui lòng kiểm tra quyền hoặc đăng nhập lại"
                    } else {
                        Log.e("CreateSpaceViewModel", "Exception: ${e.message}")
                        _error.value = e.message ?: "Lỗi khi tạo không gian"
                    }
                }
            } catch (e: Exception) {
                Log.e("CreateSpaceViewModel", "Exception: ${e.message}")
                _error.value = e.message ?: "Lỗi khi tạo không gian"
            } finally {
                _isLoading.value = false
            }
        }
    }

    // Xóa lỗi
    fun clearError() {
        _error.value = null
    }

    // Reset state thành công
    fun resetSuccess() {
        _isSuccess.value = false
    }
}