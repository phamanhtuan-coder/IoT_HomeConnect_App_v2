package com.sns.homeconnect_v2.presentation.viewmodel.group

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sns.homeconnect_v2.data.remote.dto.request.CreateGroupRequest
import com.sns.homeconnect_v2.domain.usecase.group.CreateGroupUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
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
    val createGroupState = _createGroupState.asStateFlow()

    fun createGroup(
        groupName: String,
        onSuccess: (String) -> Unit,
        onError: (String) -> Unit
    ) {
        _createGroupState.value = CreateGroupState.Loading
        viewModelScope.launch {
            val result = createGroupUseCase(CreateGroupRequest(group_name = groupName))
            result.fold(
                onSuccess = {
                    _createGroupState.value = CreateGroupState.Success
                    onSuccess("Tạo nhóm thành công!")
                },
                onFailure = {
                    _createGroupState.value = CreateGroupState.Error(it.message ?: "Tạo nhóm thất bại!")
                    onError(it.message ?: "Tạo nhóm thất bại!")
                }
            )
        }
    }
}
