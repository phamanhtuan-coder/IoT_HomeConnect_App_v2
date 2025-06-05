package com.sns.homeconnect_v2.presentation.viewmodel.group

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Group
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sns.homeconnect_v2.domain.usecase.group.GetMyGroupsUseCase
import com.sns.homeconnect_v2.presentation.model.GroupUi
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class GroupListViewModel @Inject constructor(
    private val getMyGroupsUseCase: GetMyGroupsUseCase
) : ViewModel() {

    private val _groupList = MutableStateFlow<List<GroupUi>>(emptyList())
    val groupList = _groupList.asStateFlow()

    fun fetchGroups() {
        viewModelScope.launch {
            val result = getMyGroupsUseCase()
            result.fold(
                onSuccess = { list ->
                    _groupList.value = list.mapIndexed { i, group ->
                        GroupUi(
                            id = group.group_id,
                            name = group.group_name,
                            members = 0, // chưa có số lượng thành viên => tạm gán 0
                            isRevealed = false,
                            icon = Icons.Default.Group,
                            iconColor = when (i % 3) {
                                0 -> Color.Blue
                                1 -> Color.Red
                                else -> Color.Green
                            }
                        )
                    }
                },
                onFailure = {
                    // TODO: xử lý lỗi nếu cần
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
