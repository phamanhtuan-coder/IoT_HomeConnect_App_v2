package com.sns.homeconnect_v2.presentation.viewmodel.house

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sns.homeconnect_v2.domain.repository.HouseRepository
import com.sns.homeconnect_v2.domain.usecase.house.DeleteHouseUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DeleteHouseViewModel @Inject constructor(
private val deleteHouseUseCase: HouseRepository,
) : ViewModel() {

    // StateFlow để thông báo trạng thái xóa cho UI
    private val _deleteState = MutableStateFlow<Result<Unit>?>(null)
    val deleteState: StateFlow<Result<Unit>?> = _deleteState.asStateFlow()

    fun deleteHouse(houseId: Int) {
        viewModelScope.launch {
            try {
                val result = deleteHouseUseCase.deleteHouse(houseId)
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