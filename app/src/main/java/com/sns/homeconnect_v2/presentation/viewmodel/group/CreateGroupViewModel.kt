package com.sns.homeconnect_v2.presentation.viewmodel.group

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sns.homeconnect_v2.data.remote.dto.request.CreateGroupRequest
import com.sns.homeconnect_v2.domain.usecase.group.CreateGroupUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class CreateGroupState {
    object Idle : CreateGroupState()
    object Loading : CreateGroupState()
    object Success : CreateGroupState()
    data class Error(val message: String) : CreateGroupState()
}

@HiltViewModel
class CreateGroupViewModel @Inject constructor(
    private val createGroupUseCase: CreateGroupUseCase
) : ViewModel() {

    private val _createGroupState = MutableStateFlow<CreateGroupState>(CreateGroupState.Idle)
    val createGroupState: StateFlow<CreateGroupState> = _createGroupState

    fun createGroup(
        groupName: String,
        groupDesc: String,
        iconName: String,
        iconColor: String,
        onSuccess: (String) -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            _createGroupState.value = CreateGroupState.Loading

            val request = CreateGroupRequest(
                group_name = groupName,
                group_description = groupDesc,
                icon_name = iconName,
                icon_color = iconColor
            )

            createGroupUseCase(request).fold(
                onSuccess = {
                    _createGroupState.value = CreateGroupState.Success
                    onSuccess(it)
                },
                onFailure = {
                    _createGroupState.value = CreateGroupState.Error(it.message ?: "Tạo nhóm thất bại")
                    onError(it.message ?: "Tạo nhóm thất bại")
                }
            )
        }
    }
}

