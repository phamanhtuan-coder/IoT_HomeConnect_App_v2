package com.sns.homeconnect_v2.presentation.viewmodel.house


import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sns.homeconnect_v2.domain.usecase.house.FetchHousesUseCase
import com.sns.homeconnect_v2.data.remote.dto.response.HousesListResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class HouseManagementState {
    data object Idle : HouseManagementState()               // Chưa làm gì
    data object Loading : HouseManagementState()            // Đang loading
    data class Success(val houses: List<HousesListResponse>) : HouseManagementState()
    data class Error(val error: String?) : HouseManagementState()
}

//sealed class UpdateHouseState {
//    data object Idle : UpdateHouseState()
//    data object Loading : UpdateHouseState()
//    data class Success(val message: String, val house: HouseDetail) : UpdateHouseState()
//    data class Error(val error: String) : UpdateHouseState()
//}
//
//sealed class CreateHouseState {
//    data object Idle : CreateHouseState()
//    data object Loading : CreateHouseState()
//    data class Success(val message: String, val house: HouseDetail1, val space: DefaultSpace) : CreateHouseState()
//    data class Error(val error: String) : CreateHouseState()
//}

@HiltViewModel
class HouseManagementViewModel @Inject constructor (
    private val fetchHousesUseCase: FetchHousesUseCase,
//    private val createHouseUseCase: CreateHouseUseCase,
//    private val updateHouseUseCase: UpdateHouseUseCase,
) : ViewModel() {
    // StateFlow để UI lắng nghe trạng thái
    private val _houseManagementState = MutableStateFlow<HouseManagementState>(HouseManagementState.Idle)
    val houseManagementState = _houseManagementState.asStateFlow()

    // Hàm lấy danh sách Houses
    fun fetchHouses() {
        _houseManagementState.value = HouseManagementState.Loading
        viewModelScope.launch {
            fetchHousesUseCase().fold(
                onSuccess = { response ->
                    Log.d("HouseManagementScreenViewModel", "Success: $response")
                    _houseManagementState.value  =  HouseManagementState.Success(response)
                },
                onFailure = { e ->
                    Log.e("HouseManagementScreenViewModel", "Error: ${e.message}")
                    _houseManagementState.value  =  HouseManagementState.Error(e.message ?: "Failed to fetch houses data")
                }
            )
        }
    }

//    private val _updateHouseState = MutableStateFlow<UpdateHouseState>(UpdateHouseState.Idle)
//    val updateHouseState = _updateHouseState.asStateFlow()
//
//    fun updateHouse(houseId: Int, request: UpdateHouseRequest) {
//        _updateHouseState.value = UpdateHouseState.Loading
//        viewModelScope.launch {
//            try {
//                // Gọi hàm cập nhật từ Repository
//                val response = repository.updateHouse(houseId, request)
//                _updateHouseState.value = UpdateHouseState.Success(response.message, response.house)
//            } catch (e: Exception) {
//                _updateHouseState.value = UpdateHouseState.Error(e.message ?: "Đã xảy ra lỗi")
//            }
//        }
//    }
//
//    private val _createHouseState = MutableStateFlow<CreateHouseState>(CreateHouseState.Idle)
//    val createHouseState = _createHouseState.asStateFlow()
//
//    fun createHouse(request: CreateHouseRequest) {
//        _createHouseState.value = CreateHouseState.Loading
//        viewModelScope.launch {
//            try {
//                val response = repository.createHouse(request)
//                _createHouseState.value = CreateHouseState.Success(response.message, response.house, response.space)
//            } catch (e: Exception) {
//                _createHouseState.value = CreateHouseState.Error(e.message ?: "Đã xảy ra lỗi")
//            }
//        }
//    }
}