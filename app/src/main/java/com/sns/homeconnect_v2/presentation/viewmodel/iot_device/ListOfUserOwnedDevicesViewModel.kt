package com.sns.homeconnect_v2.presentation.viewmodel.iot_device

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sns.homeconnect_v2.data.remote.dto.response.OwnedDeviceResponse
import com.sns.homeconnect_v2.domain.usecase.iot_device.ListOfUserOwnedDevicesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class ListOfUserOwnedDevicesState {
   data object Idle : ListOfUserOwnedDevicesState()
   data object Loading : ListOfUserOwnedDevicesState()
   data class Success(val deviceList: List<OwnedDeviceResponse>) : ListOfUserOwnedDevicesState()
   data class Error(val error: String) : ListOfUserOwnedDevicesState()
}

@HiltViewModel
class ListOfUserOwnedDevicesViewModel @Inject constructor(
   private val listOfUserOwnedDevicesUseCase: ListOfUserOwnedDevicesUseCase
) : ViewModel() {
   private val _listOfUserOwnedDevicesState = MutableStateFlow<ListOfUserOwnedDevicesState>(ListOfUserOwnedDevicesState.Idle)
   val listOfUserOwnedDevicesState: StateFlow<ListOfUserOwnedDevicesState> = _listOfUserOwnedDevicesState.asStateFlow()

   fun getListOfUserOwnedDevices() {
      searchOwnedDevices("") // Gọi với query rỗng để lấy tất cả
   }

   fun searchOwnedDevices(query: String) {
      _listOfUserOwnedDevicesState.value = ListOfUserOwnedDevicesState.Loading
      viewModelScope.launch {
         listOfUserOwnedDevicesUseCase(query).fold(
            onSuccess = { responseList ->
               _listOfUserOwnedDevicesState.value = ListOfUserOwnedDevicesState.Success(responseList)
            },
            onFailure = { error ->
               _listOfUserOwnedDevicesState.value = ListOfUserOwnedDevicesState.Error(error.message ?: "Lỗi không xác định")
            }
         )
      }
   }

   fun updateRevealState(index: Int) {
      (_listOfUserOwnedDevicesState.value as? ListOfUserOwnedDevicesState.Success)?.let { state ->
         val updatedList = state.deviceList.mapIndexed { i, device ->
            device.copy(isRevealed = i == index)
         }
         _listOfUserOwnedDevicesState.value = ListOfUserOwnedDevicesState.Success(updatedList)
      }
   }

   fun collapseItem(index: Int) {
      (_listOfUserOwnedDevicesState.value as? ListOfUserOwnedDevicesState.Success)?.let { state ->
         val updatedList = state.deviceList.mapIndexed { i, device ->
            if (i == index) device.copy(isRevealed = false) else device
         }
         _listOfUserOwnedDevicesState.value = ListOfUserOwnedDevicesState.Success(updatedList)
      }
   }
}