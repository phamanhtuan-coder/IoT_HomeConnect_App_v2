package com.sns.homeconnect_v2.data.repository

import android.content.Context
import com.sns.homeconnect_v2.data.AuthManager
import com.sns.homeconnect_v2.data.remote.api.ApiService
import com.sns.homeconnect_v2.data.remote.dto.response.HouseResponse
import com.sns.homeconnect_v2.domain.repository.HouseRepository
import javax.inject.Inject


class HouseRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val authManager: AuthManager
) : HouseRepository {

    override suspend fun getListHome(): List<HouseResponse> {
        val token = authManager.getJwtToken()
        return apiService.getListHome(token = "Bearer $token")
    }

//    override suspend fun getHouses(): List<HousesListPesponse> {
//        // Lấy token từ SharedPreferences
//        val token = authManager.getJwtToken()
//
//        // Gọi API với token
//        return apiService.getHouses(token = "Bearer $token")
//    }
//
//    override suspend fun updateHouse(houseID: Int, request: UpdateHouseRequest): UpdateHouseResponse {
//        // Lấy token từ SharedPreferences
//        val token = authManager.getJwtToken()
//
//        // Gọi API với token
//        return apiService.updateHouse(houseID, request, token = "Bearer $token")
//    }
//
//    override suspend fun createHouse(request: CreateHouseRequest): CreateHouseResponse {
//        // Lấy token từ SharedPreferences
//        val token = authManager.getJwtToken()
//
//        // Gọi API với token
//        return apiService.createHouse(request, "Bearer $token")
//    }
}