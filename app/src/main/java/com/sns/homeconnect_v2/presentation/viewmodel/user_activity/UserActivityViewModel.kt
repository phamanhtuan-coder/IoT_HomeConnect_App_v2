package com.sns.homeconnect_v2.presentation.viewmodel.user_activity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sns.homeconnect_v2.data.remote.dto.response.UserActivityResponse
import com.sns.homeconnect_v2.domain.usecase.user_activity.GetUserActivitiesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class UserActivityViewModel @Inject constructor(
    private val getUserActivitiesUseCase: GetUserActivitiesUseCase
) : ViewModel() {
    private val _activities = MutableStateFlow<List<UserActivityResponse>>(emptyList())
    val activities = _activities.asStateFlow()

    fun loadActivities() {
        viewModelScope.launch {
            _activities.value = getUserActivitiesUseCase()
        }
    }
}
