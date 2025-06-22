package com.sns.homeconnect_v2.data.repository

import com.sns.homeconnect_v2.data.AuthManager
import com.sns.homeconnect_v2.data.remote.api.ApiService
import com.sns.homeconnect_v2.data.remote.dto.response.HouseWithSpacesResponse
import com.sns.homeconnect_v2.data.remote.dto.request.CreateHouseRequest
import com.sns.homeconnect_v2.data.remote.dto.response.house.CreateHouseResponse
import com.sns.homeconnect_v2.data.remote.dto.response.house.House
import com.sns.homeconnect_v2.data.remote.dto.response.house.HouseResponse
import com.sns.homeconnect_v2.data.remote.dto.response.house.HousesListResponse
import com.sns.homeconnect_v2.data.remote.dto.response.house.UpdateHouseRequest
import com.sns.homeconnect_v2.data.remote.dto.response.house.UpdateHouseResponse
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

    override suspend fun getHouseById(houseId: Int): House {
        // Lấy token từ SharedPreferences
        val token = authManager.getJwtToken()
        // Gọi API với token
        return apiService.getHouseId(houseId, token = "Bearer $token")
    }

    override suspend fun getHouses(): List<HousesListResponse>{
        // Lấy token từ SharedPreferences
        val token = authManager.getJwtToken()
        // Gọi API với token
        return apiService.getHouses(token = "Bearer $token")
    }

    override suspend fun updateHouse(houseID: Int, request: UpdateHouseRequest): UpdateHouseResponse {
        // Lấy token từ SharedPreferences
        val token = authManager.getJwtToken()

        // Gọi API với token
        return apiService.updateHouse(houseID, request, token = "Bearer $token")
    }

    override suspend fun createHouse(request: CreateHouseRequest): CreateHouseResponse {
        // Lấy token từ SharedPreferences
        val token = authManager.getJwtToken()

        // Gọi API với token
        return apiService.createHouse(request, "Bearer $token")
    }

    override suspend fun getHousesByGroupId(groupId: Int): List<HouseWithSpacesResponse> {
        val token = authManager.getJwtToken()
        return apiService.getHousesByGroupId(groupId, "Bearer $token")
    }

    override suspend fun deleteHouse(houseId: Int): Result<Unit> {
        val token = authManager.getJwtToken()
        return try {
            apiService.deleteHouse(houseId, "Bearer $token")
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}