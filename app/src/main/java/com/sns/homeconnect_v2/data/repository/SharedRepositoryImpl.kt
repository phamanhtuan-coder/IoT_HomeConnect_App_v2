package com.sns.homeconnect_v2.data.repository


import com.sns.homeconnect_v2.data.AuthManager
import com.sns.homeconnect_v2.data.remote.api.ApiService
import com.sns.homeconnect_v2.data.remote.dto.request.ApproveRequest
import com.sns.homeconnect_v2.data.remote.dto.response.SharedDeviceResponse
import com.sns.homeconnect_v2.data.remote.dto.response.SharedUser
import com.sns.homeconnect_v2.data.remote.dto.response.SharedUserRequest
import com.sns.homeconnect_v2.domain.repository.SharedRepository
import retrofit2.Response
import javax.inject.Inject

class SharedRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val authManager: AuthManager
): SharedRepository {
    override suspend fun getSharedDevicesForUser(): List<SharedDeviceResponse> {
        val token = authManager.getJwtToken()
        return apiService.getSharedDevicesForUser("Bearer $token").data
    }

    override suspend fun getSharedUsers(deviceId: Int): List<SharedUser> {
        val token = authManager.getJwtToken()
        return apiService.getSharedUsers(deviceId, token = "Bearer $token")
    }

    override suspend fun addSharedUser(deviceId: Int, sharedWithUserEmail: String): Response<Unit> {
        val token = authManager.getJwtToken()
        val request = SharedUserRequest(sharedWithUserEmail)
        return apiService.shareDevice(deviceId, request, token = "Bearer $token")
    }

    override suspend fun revokePermission(permissionID: Int):   Response<Unit> {
        val token = authManager.getJwtToken()
        return apiService.revokePermission(permissionId = permissionID, token = "Bearer $token")
    }

    override suspend fun approveSharePermission(ticketId: String): Response<Unit> {
        val token = authManager.getJwtToken()
        val approveRequest = ApproveRequest(ticketId = ticketId, isApproved = true)
        return apiService.approveSharePermission(
            request = approveRequest,
            token = "Bearer $token"
        )
    }

    override suspend fun revokeRecipientPermission(serialNumber: String): Response<Unit> {
        val token = "Bearer ${authManager.getJwtToken()}"
        return apiService.revokeRecipientPermission(serialNumber, token)
    }
}