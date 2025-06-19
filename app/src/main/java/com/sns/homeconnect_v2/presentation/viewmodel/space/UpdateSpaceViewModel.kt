package com.sns.homeconnect_v2.presentation.viewmodel.space

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sns.homeconnect_v2.data.remote.dto.response.SpaceResponse
import com.sns.homeconnect_v2.domain.usecase.space.UpdateSpaceUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UpdateSpaceViewModel @Inject constructor(
    private val updateSpaceUseCase: UpdateSpaceUseCase
) : ViewModel() {
    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _updatespace = MutableStateFlow<SpaceResponse?>(null)
    val updatespace = _updatespace.asStateFlow()

    fun updateSpace(
        spaceId: Int,
        name: String,
        iconName: String? = null,
        iconColor: String? = null,
        description: String? = null
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val result = updateSpaceUseCase.invoke(spaceId, name, iconName, iconColor, description)
                result.onSuccess { spaces ->
                    _updatespace.value = spaces
                    Log.d("ViewModel space", spaces.toString())
                }.onFailure { error ->
                    error.printStackTrace()
                    Log.e("ViewModel error", error.toString())
                }
            } finally {
                _isLoading.value = false
            }
        }
    }
}