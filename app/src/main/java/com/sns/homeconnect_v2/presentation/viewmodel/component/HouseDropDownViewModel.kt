package com.sns.homeconnect_v2.presentation.viewmodel.component

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sns.homeconnect_v2.domain.usecase.home.GetListHouseUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import com.sns.homeconnect_v2.data.remote.dto.response.HouseResponse
import javax.inject.Inject

sealed class HouseState {
    data object Idle : HouseState()              // Chưa làm gì
    data object Loading : HouseState()            // Đang loading
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

    // Lưu trữ houseId
    private val _houseId = MutableStateFlow<Int?>(null)
    val houseId: StateFlow<Int?> get() = _houseId

    // Lưu trữ userId
    private val _userId = MutableStateFlow<Int?>(null)
    val userId: StateFlow<Int?> get() = _userId

    // Thiết lập houseId
    fun setHouseId(id: Int) {
        _houseId.value = id
    }

    // Thiết lập userId
    fun setUserId(id: Int) {
        _userId.value = id
    }

    // Xóa houseId
    fun clearHouseId() {
        _houseId.value = null // Xóa giá trị khi không cần nữa
    }

    // Xóa userId
    fun clearUserId() {
        _userId.value = null // Xóa giá trị khi không cần nữa
    }

    // Xóa cả houseId và userId
    fun clearAll() {
        _houseId.value = null
        _userId.value = null
    }
}