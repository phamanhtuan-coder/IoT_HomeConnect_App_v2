package com.sns.homeconnect_v2.presentation.viewmodel.house

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sns.homeconnect_v2.data.remote.dto.response.house.House
import com.sns.homeconnect_v2.domain.usecase.house.GetHouseUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class GetHouseViewModel @Inject constructor(
private val getHouseUseCase: GetHouseUseCase
) : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _houses = MutableStateFlow<House?>(null)
    val houses = _houses.asStateFlow()

    fun getHouse(houseId: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            getHouseUseCase.invoke(houseId).onSuccess { house ->
                _houses.value = house
            }.onFailure { error ->
                error.printStackTrace()
            }
            _isLoading.value = false
        }
    }

}