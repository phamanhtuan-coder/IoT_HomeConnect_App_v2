package com.sns.homeconnect_v2.presentation.viewmodel.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sns.homeconnect_v2.data.remote.dto.response.SharedWithResponse
import com.sns.homeconnect_v2.data.remote.dto.response.house.CreateHouseResponse
import com.sns.homeconnect_v2.domain.usecase.home.FetchSharedWithUseCase
import com.sns.homeconnect_v2.domain.usecase.house.CreateHouseUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class SharedWithState {
    data object Idle : SharedWithState()
    data object Loading : SharedWithState()
    data class Success(val sharedWithResponse: List<SharedWithResponse>) : SharedWithState()
    data class Error(val error: String) : SharedWithState()
}

sealed class CreateHouseState {
    data object Idle : CreateHouseState()
    data object Loading : CreateHouseState()
    data class Success(val response: CreateHouseResponse) : CreateHouseState()
    data class Error(val error: String) : CreateHouseState()
}

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val fetchSharedWithUseCase: FetchSharedWithUseCase,
    private val createHouseUseCase: CreateHouseUseCase,
) : ViewModel() {

    private val _sharedWithState = MutableStateFlow<SharedWithState>(SharedWithState.Idle)
    val sharedWithState: StateFlow<SharedWithState> = _sharedWithState

    private val _createHouseState = MutableStateFlow<CreateHouseState>(CreateHouseState.Idle)
    val createHouseState: StateFlow<CreateHouseState> = _createHouseState

    fun fetchSharedWith(userId: Int) {
        _sharedWithState.value = SharedWithState.Loading
        viewModelScope.launch {
            fetchSharedWithUseCase(userId).fold(
                onSuccess = { response ->
                    _sharedWithState.value = SharedWithState.Success(response)
                },
                onFailure = { e ->
                    _sharedWithState.value = SharedWithState.Error(e.message ?: "Failed to fetch shared with data")
                }
            )
        }
    }

    fun createHouse(
        groupId: Int,
        houseName: String,
        address: String,
        iconName: String,
        iconColor: String
    ) {
        _createHouseState.value = CreateHouseState.Loading
        viewModelScope.launch {
            createHouseUseCase(
                groupId = groupId,
                houseName = houseName,
                address = address,
                iconName = iconName,
                iconColor = iconColor
            ).fold(
                onSuccess = { response ->
                    _createHouseState.value = CreateHouseState.Success(response)
                },
                onFailure = { e ->
                    _createHouseState.value = CreateHouseState.Error(e.message ?: "Failed to create house")
                }
            )
        }
    }
}