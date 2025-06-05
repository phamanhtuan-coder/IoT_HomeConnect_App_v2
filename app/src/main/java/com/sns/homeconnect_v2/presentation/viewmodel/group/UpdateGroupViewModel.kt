package com.sns.homeconnect_v2.presentation.viewmodel.group

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sns.homeconnect_v2.data.remote.dto.request.UpdateGroupRequest
import com.sns.homeconnect_v2.domain.usecase.group.UpdateGroupUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class UpdateGroupViewModel @Inject constructor(
    private val updateGroupUseCase: UpdateGroupUseCase
) : ViewModel() {

    fun updateGroup(
        groupId: Int,
        groupName: String,
        onSuccess: (String) -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            val result = updateGroupUseCase(groupId, UpdateGroupRequest(group_name = groupName))
            result.fold(
                onSuccess = {
                    onSuccess("Cập nhật nhóm thành công!")
                },
                onFailure = {
                    onError(it.message ?: "Cập nhật nhóm thất bại!")
                }
            )
        }
    }
}
