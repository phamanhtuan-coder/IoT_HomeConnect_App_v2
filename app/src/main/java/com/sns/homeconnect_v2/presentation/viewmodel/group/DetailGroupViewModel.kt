package com.sns.homeconnect_v2.presentation.viewmodel.group

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sns.homeconnect_v2.data.remote.dto.response.MemberResponse
import com.sns.homeconnect_v2.domain.usecase.group.GetGroupMembersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import androidx.lifecycle.SavedStateHandle
import com.sns.homeconnect_v2.data.remote.dto.response.HouseWithSpacesResponse
import com.sns.homeconnect_v2.domain.usecase.house.GetHousesByGroupUseCase

@HiltViewModel
class DetailGroupViewModel @Inject constructor(
    private val memberUseCase: GetGroupMembersUseCase,
    private val getHousesByGroupUseCase: GetHousesByGroupUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    val groupId: Int = savedStateHandle["groupId"] ?: -1

    private val _members = MutableStateFlow<List<MemberResponse>>(emptyList())
    val members = _members.asStateFlow()

    private val _houses = MutableStateFlow<List<HouseWithSpacesResponse>>(emptyList())
    val houses = _houses.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    init {
        fetchGroupMembers()
        fetchHouses()
    }

    fun fetchGroupMembers() {
        _isLoading.value = true
        viewModelScope.launch {
            val result = memberUseCase(groupId)
            result.fold(
                onSuccess = { _members.value = it },
                onFailure = { _members.value = emptyList() }
            )
            _isLoading.value = false
        }
    }

    fun fetchHouses() {
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
