package com.sns.homeconnect_v2.presentation.viewmodel.component

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sns.homeconnect_v2.data.remote.dto.response.house.HouseResponse
import com.sns.homeconnect_v2.domain.usecase.home.GetListHouseUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class HouseState {
    data object Idle : HouseState()
    data object Loading : HouseState()
    data class Success(val houseList: List<HouseResponse>) : HouseState()
    data class Error(val error: String) : HouseState()
}

@HiltViewModel
class HouseDropDownViewModel @Inject constructor(
    private val getListHouseUseCase: GetListHouseUseCase,
) : ViewModel() {

    private val _houseListState = MutableStateFlow<HouseState>(HouseState.Idle)
    val houseListState = _houseListState.asStateFlow()

    /**
     * Lấy danh sách House
     */
    fun getListHouse() {
        _houseListState.value = HouseState.Loading
        viewModelScope.launch {
            getListHouseUseCase().fold(
                onSuccess = { response ->
                    _houseListState.value = HouseState.Success(response)
                },
                onFailure = { error ->
                    Log.e("HouseDropDownViewModel", "Error fetching houses: ${error.message}")
                    _houseListState.value =
                        HouseState.Error(error.message ?: "Danh sach load thất bại!")
                }
            )
        }
    }
}

@HiltViewModel
class SharedViewModel @Inject constructor() : ViewModel() {

    private val _houseId = MutableStateFlow<Int?>(null)
    val houseId: StateFlow<Int?> get() = _houseId

    private val _userId = MutableStateFlow<Int?>(null)
    val userId: StateFlow<Int?> get() = _userId

    fun setHouseId(id: Int) {
        _houseId.value = id
    }

    fun setUserId(id: Int) {
        _userId.value = id
    }

    fun clearHouseId() {
        _houseId.value = null
    }

    fun clearUserId() {
        _userId.value = null
    }

    fun clearAll() {
        _houseId.value = null
        _userId.value = null
    }
}