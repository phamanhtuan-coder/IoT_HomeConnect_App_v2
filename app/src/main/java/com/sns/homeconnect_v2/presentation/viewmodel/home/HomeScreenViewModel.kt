package com.sns.homeconnect_v2.presentation.viewmodel.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sns.homeconnect_v2.data.remote.dto.response.SharedWithResponse
import com.sns.homeconnect_v2.domain.usecase.home.FetchSharedWithUseCase
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

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val fetchSharedWithUseCase: FetchSharedWithUseCase,
) : ViewModel() {

    private val _sharedWithState = MutableStateFlow<SharedWithState>(SharedWithState.Idle)
    val sharedWithState: StateFlow<SharedWithState> = _sharedWithState

    fun fetchSharedWith(userId: Int) {
        _sharedWithState.value = SharedWithState.Loading
        viewModelScope.launch {
            fetchSharedWithUseCase(userId).fold(
                onSuccess = { response ->
                    Log.d("HomeScreenViewModel", "Success: $response")
                    _sharedWithState.value = SharedWithState.Success(response)
                },
                onFailure = { e ->
                    Log.e("HomeScreenViewModel", "Error: ${e.message}")
                    _sharedWithState.value = SharedWithState.Error(e.message ?: "Failed to fetch shared with data")
                }
            )
        }
    }
}