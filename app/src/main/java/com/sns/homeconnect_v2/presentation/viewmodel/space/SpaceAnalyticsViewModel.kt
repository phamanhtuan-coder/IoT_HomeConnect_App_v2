package com.sns.homeconnect_v2.presentation.viewmodel.space

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sns.homeconnect_v2.data.remote.dto.response.HourlyValueResponse
import com.sns.homeconnect_v2.domain.usecase.hourly_values.GetHourlyValuesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class SpaceAnalyticsViewModel @Inject constructor(
    private val getHourlyValues: GetHourlyValuesUseCase
) : ViewModel() {

    private val _values = MutableStateFlow<List<HourlyValueResponse>>(emptyList())
    val values = _values.asStateFlow()

    fun loadHourlyValues(spaceId: Int) = viewModelScope.launch {
        val start = "2025-06-16T00:00:00Z"
        val end   = "2025-06-21T00:00:00Z"
        _values.value = getHourlyValues(spaceId, start, end)
    }
}
