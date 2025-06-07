package com.sns.homeconnect_v2.presentation.viewmodel.house

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sns.homeconnect_v2.data.remote.dto.response.HouseWithSpacesResponse
import com.sns.homeconnect_v2.domain.usecase.house.GetHousesByGroupUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HouseSearchViewModel @Inject constructor(
    private val getHousesByGroupUseCase: GetHousesByGroupUseCase,
) : ViewModel() {
    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _houses = MutableStateFlow<List<HouseWithSpacesResponse>>(emptyList())
    val houses = _houses.asStateFlow()

    fun loadHousesByGroup(groupId: Int = 5) {
        viewModelScope.launch {
            _isLoading.value = true
            getHousesByGroupUseCase(groupId).fold(
                onSuccess = {
                    _houses.value = it
                },
                onFailure = {
                    _houses.value = emptyList()
                }
            )
            _isLoading.value = false
        }
    }
}