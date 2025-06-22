package com.sns.homeconnect_v2.presentation.viewmodel.group

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sns.homeconnect_v2.data.remote.dto.response.RoleResponse
import com.sns.homeconnect_v2.domain.usecase.group.GetRoleUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GetRoleViewModel @Inject constructor(
    private val getRoleUseCase: GetRoleUseCase
): ViewModel(){

    // Lưu roles theo groupId - Map<groupId, RoleResponse>
    private val _roles = MutableStateFlow<Map<Int, RoleResponse>>(emptyMap())
    val roles: StateFlow<Map<Int, RoleResponse>> = _roles.asStateFlow()

    // Lưu trạng thái loading theo groupId
    private val _loadingStates = MutableStateFlow<Map<Int, Boolean>>(emptyMap())
    val loadingStates: StateFlow<Map<Int, Boolean>> = _loadingStates.asStateFlow()

    // Lưu lỗi theo groupId
    private val _errors = MutableStateFlow<Map<Int, String>>(emptyMap())
    val errors: StateFlow<Map<Int, String>> = _errors.asStateFlow()

    fun fetchRole(groupId: Int) {
        // Kiểm tra nếu đã có role cho group này thì không gọi lại
        if (_roles.value.containsKey(groupId)) {
            return
        }

        // Set loading state
        _loadingStates.value = _loadingStates.value + (groupId to true)

        viewModelScope.launch {
            getRoleUseCase(groupId).onSuccess { roleResponse ->
                // Lưu role cho groupId này
                _roles.value = _roles.value + (groupId to roleResponse)

                // Xóa lỗi nếu có
                _errors.value = _errors.value - groupId

                // Tắt loading
                _loadingStates.value = _loadingStates.value + (groupId to false)

            }.onFailure { exception ->
                // Lưu lỗi cho groupId này
                val errorMessage = exception.message ?: "Lỗi không xác định khi lấy vai trò"
                _errors.value = _errors.value + (groupId to errorMessage)

                // Tắt loading
                _loadingStates.value = _loadingStates.value + (groupId to false)
            }
        }
    }

    // Lấy role theo groupId
    fun getRoleByGroupId(groupId: Int): String? {
        return _roles.value[groupId]?.role
    }

    // Kiểm tra xem có đang loading role cho group này không
    fun isLoadingRole(groupId: Int): Boolean {
        return _loadingStates.value[groupId] ?: false
    }

    // Lấy lỗi theo groupId
    fun getErrorByGroupId(groupId: Int): String? {
        return _errors.value[groupId]
    }

    // Xóa role của một group (khi cần thiết)
    fun clearRoleForGroup(groupId: Int) {
        _roles.value = _roles.value - groupId
        _errors.value = _errors.value - groupId
        _loadingStates.value = _loadingStates.value - groupId
    }

    // Xóa tất cả roles (khi logout chẳng hạn)
    fun clearAllRoles() {
        _roles.value = emptyMap()
        _errors.value = emptyMap()
        _loadingStates.value = emptyMap()
    }

    // Force refresh role cho một group
    fun refreshRole(groupId: Int) {
        clearRoleForGroup(groupId)
        fetchRole(groupId)
    }
}