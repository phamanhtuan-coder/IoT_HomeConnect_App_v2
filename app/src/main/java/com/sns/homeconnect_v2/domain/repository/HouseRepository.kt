package com.sns.homeconnect_v2.domain.repository

import com.sns.homeconnect_v2.data.remote.dto.response.HouseResponse


interface HouseRepository {

    suspend fun getListHome(): List<HouseResponse>

//    suspend fun getHouses(): List<HousesListPesponse>
//    suspend fun updateHouse(houseID: Int, request: UpdateHouseRequest): UpdateHouseResponse
//    suspend fun createHouse(request: CreateHouseRequest): CreateHouseResponse
}