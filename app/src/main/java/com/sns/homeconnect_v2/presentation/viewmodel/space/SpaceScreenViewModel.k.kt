package com.sns.homeconnect_v2.presentation.viewmodel.space

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sns.homeconnect_v2.data.remote.dto.response.Space
import com.sns.homeconnect_v2.data.remote.dto.response.SpaceResponse
import com.sns.homeconnect_v2.domain.usecase.space.GetListSpaceUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class SpaceScreenViewModel @Inject constructor(
    private val getListSpaceUseCase: GetListSpaceUseCase,
) : ViewModel() {
    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _spaces = MutableStateFlow<List<SpaceResponse>>(emptyList())
    val spaces = _spaces.asStateFlow()

    fun getSpaces(houseId: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            val result = getListSpaceUseCase.invoke(houseId)
            result.onSuccess { spaces ->
                _spaces.value = spaces // Đảm bảo spaces là List<Space>
                Log.d("ViewModel space", spaces.toString())
            }.onFailure { error ->
                error.printStackTrace()
                Log.e("ViewModel error", error.toString())
            }
            _isLoading.value = false
        }
    }
    fun updateRevealState(index: Int) {
        _spaces.value = _spaces.value.mapIndexed { i, item ->
            item.copy(isRevealed = i == index)
        }
    }

    fun collapseItem(index: Int) {
        _spaces.value = _spaces.value.mapIndexed { i, item ->
            if (i == index) item.copy(isRevealed = false) else item
        }
    }

    fun expandItem(index: Int) {
        _spaces.update { currentSpaces ->
            currentSpaces.mapIndexed { i, space ->
                if (i == index) space.copy(isRevealed = true)
                else space.copy(isRevealed = false)

            }
        }
    }
}