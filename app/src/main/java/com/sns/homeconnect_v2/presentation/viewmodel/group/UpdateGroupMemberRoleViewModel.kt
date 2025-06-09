package com.sns.homeconnect_v2.presentation.viewmodel.group

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sns.homeconnect_v2.domain.usecase.group.UpdateGroupMemberRoleUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class UpdateGroupMemberRoleUiState {
    data object Idle : UpdateGroupMemberRoleUiState()
    data object Loading : UpdateGroupMemberRoleUiState()
    data object Success : UpdateGroupMemberRoleUiState()
    data class Error(val message: String) : UpdateGroupMemberRoleUiState()
}

@HiltViewModel
class UpdateGroupMemberRoleViewModel @Inject constructor(
    private val updateGroupMemberRoleUseCase: UpdateGroupMemberRoleUseCase
) : ViewModel() {

    private val _updateState = MutableStateFlow<UpdateGroupMemberRoleUiState>(UpdateGroupMemberRoleUiState.Idle)
    val updateState = _updateState.asStateFlow()

    fun updateMemberRole(groupId: Int, accountId: String, role: String) {
        _updateState.value = UpdateGroupMemberRoleUiState.Loading

        viewModelScope.launch {
            updateGroupMemberRoleUseCase(groupId, accountId, role).fold(
                onSuccess = {
                    _updateState.value = UpdateGroupMemberRoleUiState.Success
                },
                onFailure = { e ->
                    _updateState.value = UpdateGroupMemberRoleUiState.Error(
                        e.message ?: "Failed to update member role"
                    )
                }
            )
        }
    }
}
