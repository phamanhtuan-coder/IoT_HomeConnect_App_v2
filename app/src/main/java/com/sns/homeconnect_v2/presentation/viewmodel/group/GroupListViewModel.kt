package com.sns.homeconnect_v2.presentation.viewmodel.group

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sns.homeconnect_v2.domain.usecase.group.GetMyGroupsUseCase
import com.sns.homeconnect_v2.presentation.model.GroupUi
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import com.sns.homeconnect_v2.core.util.validation.mapGroupResponseToUi
import com.sns.homeconnect_v2.data.AuthManager
import com.sns.homeconnect_v2.domain.usecase.group.GetGroupMembersUseCase

@HiltViewModel
class GroupListViewModel @Inject constructor(
    private val getMyGroupsUseCase: GetMyGroupsUseCase,
    private val getGroupMembersUseCase: GetGroupMembersUseCase,
    private val authManager: AuthManager
) : ViewModel() {

    private val _groupList = MutableStateFlow<List<GroupUi>>(emptyList())
    val groupList = _groupList.asStateFlow()

    fun fetchGroups() {
        viewModelScope.launch {
            val result = getMyGroupsUseCase()
            result.fold(
                onSuccess = { list ->
                    val groupUis = list.map { group ->
                        mapGroupResponseToUi(
                            group = group,
                            memberUseCase = getGroupMembersUseCase,
                            authManager = authManager
                        )
                    }
                    _groupList.value = groupUis
                },
                onFailure = {
                }
            )
        }
    }

    fun updateRevealState(index: Int) {
        _groupList.value = _groupList.value.mapIndexed { i, item ->
            item.copy(isRevealed = i == index)
        }
    }

    fun collapseItem(index: Int) {
        _groupList.value = _groupList.value.mapIndexed { i, item ->
            if (i == index) item.copy(isRevealed = false) else item
        }
    }
}
