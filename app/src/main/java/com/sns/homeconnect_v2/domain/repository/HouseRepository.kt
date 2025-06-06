package com.sns.homeconnect_v2.domain.repository

import com.sns.homeconnect_v2.data.remote.dto.response.HouseWithSpacesResponse
import com.sns.homeconnect_v2.data.remote.dto.response.house.CreateHouseRequest
import com.sns.homeconnect_v2.data.remote.dto.response.house.CreateHouseResponse
import com.sns.homeconnect_v2.data.remote.dto.response.house.HouseResponse
import com.sns.homeconnect_v2.data.remote.dto.response.house.HousesListResponse
import com.sns.homeconnect_v2.data.remote.dto.response.house.UpdateHouseRequest
import com.sns.homeconnect_v2.data.remote.dto.response.house.UpdateHouseResponse


interface HouseRepository {
    suspend fun getListHome(): List<HouseResponse>
    suspend fun getHouses(): List<HousesListResponse>
    suspend fun updateHouse(houseID: Int, request: UpdateHouseRequest): UpdateHouseResponse
    suspend fun createHouse(request: CreateHouseRequest): CreateHouseResponse
    suspend fun getHousesByGroupId(groupId: Int): List<HouseWithSpacesResponse>
}