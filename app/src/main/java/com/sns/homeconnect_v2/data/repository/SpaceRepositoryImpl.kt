package com.sns.homeconnect_v2.data.repository

import android.util.Log
import com.sns.homeconnect_v2.data.AuthManager
import com.sns.homeconnect_v2.data.remote.api.ApiService
import com.sns.homeconnect_v2.data.remote.dto.request.CreateSpaceRequest
import com.sns.homeconnect_v2.data.remote.dto.request.UpdateSpaceRequest
import com.sns.homeconnect_v2.data.remote.dto.response.SpaceResponse
import com.sns.homeconnect_v2.data.remote.dto.response.DeviceResponse
import com.sns.homeconnect_v2.data.remote.dto.response.DeviceResponseSpace
import com.sns.homeconnect_v2.domain.repository.SpaceRepository
import javax.inject.Inject

class SpaceRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val authManager: AuthManager
) : SpaceRepository {
    /** Lấy toàn bộ Space thuộc một House */
    override suspend fun getSpacesByHomeId(homeId: Int): List<SpaceResponse> {
        val token = authManager.getJwtToken().orEmpty()
        return apiService.getSpaces(homeId, token = "Bearer $token")
    }

    /** Lấy danh sách Device nằm trong một Space */
    override suspend fun getDevicesBySpaceId(spaceId: Int): List<DeviceResponseSpace> {
        val token = authManager.getJwtToken().orEmpty()
        return apiService.getDevicesBySpaceId(spaceId, token = "Bearer $token")
    }

    /** Tạo Space mới */
    override suspend fun createSpace(request: CreateSpaceRequest): SpaceResponse {
        val token = authManager.getJwtToken()
        Log.d("SpaceRepositoryImpl", "Token: $token")
        return apiService.createSpace(request, token = "Bearer $token")
    }

    /** Xoá Space */
    override suspend fun deleteSpace(spaceId: Int): Result<Unit> {
        return runCatching {
            val token = authManager.getJwtToken().orEmpty()
            apiService.deleteSpace(spaceId, token = "Bearer $token")
        }
    }

    /** Cập nhật Space */
    override suspend fun updateSpace(
        spaceId: Int,
        name: String,
        iconName: String?,
        iconColor: String?,
        description: String?
    ): Result<SpaceResponse> {
        val token = authManager.getJwtToken().orEmpty()
        val body = UpdateSpaceRequest(
            space_name       = name,
            icon_name        = iconName,
            icon_color       = iconColor,
            space_description = description
        )
        return runCatching {
            apiService.updateSpace(spaceId, body, token = "Bearer $token")
        }
    }
}

//    override suspend fun getSpaces(houseId: Int): List<SpaceResponse2> {
//        val token = authManager.getJwtToken()
//        return apiService.getSpaces(houseId, "Bearer $token")
//    }
//

//    override suspend fun createSpace(houseId: Int, name: String): CreateSpaceResponse {
//        val token = authManager.getJwtToken()
//        val createSpaceRequest = CreateSpaceRequest(HouseID = houseId, Name = name)
//        return apiService.createSpace(createSpaceRequest ,token = "Bearer $token")
//    }
//
//    override suspend fun getSpaceDetail(spaceId: Int): SpaceDetailResponse {
//        val token = authManager.getJwtToken()
//        return apiService.getSpaceDetail(spaceId, "Bearer $token")
//    }
//}