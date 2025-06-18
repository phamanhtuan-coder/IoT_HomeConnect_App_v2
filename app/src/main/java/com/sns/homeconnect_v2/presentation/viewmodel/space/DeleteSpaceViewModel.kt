package com.sns.homeconnect_v2.presentation.viewmodel.space

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sns.homeconnect_v2.core.util.validation.mapGroupResponseToUi
import com.sns.homeconnect_v2.domain.repository.SpaceRepository
import com.sns.homeconnect_v2.domain.usecase.space.DeleteSpaceUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DeleteSpaceViewModel @Inject constructor(
private val deleteSpaceUseCase: DeleteSpaceUseCase
) : ViewModel() {

    // StateFlow để thông báo trạng thái xóa cho UI
    private val _deleteState = MutableStateFlow<Result<Unit>?>(null)
    val deleteState: StateFlow<Result<Unit>?> = _deleteState.asStateFlow()

    fun deleteSpace(spaceId: Int) {
        viewModelScope.launch {
            try {
                val result = deleteSpaceUseCase(spaceId)
                _deleteState.value = result
            } catch (e: Exception) {
                _deleteState.value = Result.failure(e)
            }
        }
    }


    fun resetDeleteState() {
        _deleteState.value = null // Reset trạng thái xóa
    }
}