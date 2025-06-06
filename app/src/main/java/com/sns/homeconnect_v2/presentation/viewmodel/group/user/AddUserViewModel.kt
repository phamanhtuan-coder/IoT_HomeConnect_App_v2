package com.sns.homeconnect_v2.presentation.viewmodel.group.user

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sns.homeconnect_v2.data.remote.dto.response.UserGroupResponse
import com.sns.homeconnect_v2.domain.usecase.group.AddGroupMemberUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class AddUserUiState {
    data object Idle : AddUserUiState()
    data object Loading : AddUserUiState()
    data class Success(val userGroupResponse: UserGroupResponse) : AddUserUiState()
    data class Error(val message: String) : AddUserUiState()
}

@HiltViewModel
class AddUserViewModel @Inject constructor(
    private val addGroupMemberUseCase: AddGroupMemberUseCase,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    // Get groupId from SavedStateHandle
    val groupId: Int = checkNotNull(savedStateHandle["groupId"]) {
        "groupId parameter not found. Have you added it to the navigation arguments?"
    }

    private val _addUserState = MutableStateFlow<AddUserUiState>(AddUserUiState.Idle)
    val addUserState = _addUserState.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
//    val searchQuery = _searchQuery.asStateFlow()
//
//    fun onSearchQueryChanged(query: String) {
//        _searchQuery.value = query
//        // TODO: Implement search logic
//    }

    fun resetAddUserState() {
        _addUserState.value = AddUserUiState.Idle
    }

    fun addMemberToGroup(accountId: String, role: String){
        _addUserState.value = AddUserUiState.Loading
        viewModelScope.launch {
            val result = addGroupMemberUseCase(groupId, accountId, role)
            result.fold(
                onSuccess = {
                    _addUserState.value = AddUserUiState.Success(it)
                },
                onFailure = {
                    _addUserState.value = AddUserUiState.Error(it.message ?: "Thêm thành viên thất bại!")
                }
            )
        }
    }
}
