package com.sns.homeconnect_v2.presentation.viewmodel.group

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sns.homeconnect_v2.data.remote.dto.response.house.House
import com.sns.homeconnect_v2.domain.usecase.group.GetListHouseByGroupUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GetListHouseByGroupViewModel @Inject constructor(
private val getHousesByGroupUseCase: GetListHouseByGroupUseCase
) : ViewModel() {

    private val _houses = MutableStateFlow<List<House>>(emptyList())
    val houses = _houses.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    fun getHouseByGroup(groupId: Int) {
        viewModelScope.launch {
                _isLoading.value = true
                val result = getHousesByGroupUseCase.invoke(groupId)
                result.onSuccess { houses ->
                    _houses.value = houses
                    Log.d("ViewModel house", houses.toString())
                }.onFailure { error ->
                    error.printStackTrace()
                    Log.e("ViewModel error", error.toString())
                }
                _isLoading.value = false
        }
    }
}