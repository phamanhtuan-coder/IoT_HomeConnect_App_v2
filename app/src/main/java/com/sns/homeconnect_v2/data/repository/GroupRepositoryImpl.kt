package com.sns.homeconnect_v2.data.repository

import com.sns.homeconnect_v2.data.AuthManager
import com.sns.homeconnect_v2.data.remote.api.ApiService
import com.sns.homeconnect_v2.data.remote.dto.base.CreateGroupResponse
import com.sns.homeconnect_v2.data.remote.dto.request.CreateGroupRequest
import com.sns.homeconnect_v2.data.remote.dto.request.UpdateGroupRequest
import com.sns.homeconnect_v2.data.remote.dto.response.UpdateGroupResponse
import com.sns.homeconnect_v2.domain.repository.GroupRepository
import com.sns.homeconnect_v2.data.remote.dto.response.GroupResponse
import javax.inject.Inject

class GroupRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val authManager: AuthManager
) : GroupRepository {

    override suspend fun createGroup(request: CreateGroupRequest): Result<CreateGroupResponse> {
        val token = authManager.getJwtToken()
        return try {
            val response = apiService.createGroup(
                request = request,
                token = "Bearer $token"
            )
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateGroup(groupId: Int, request: UpdateGroupRequest): UpdateGroupResponse {
        val token = authManager.getJwtToken()
        return apiService.updateGroup(groupId, request, "Bearer $token")
    }

    override suspend fun getMyGroups(): Result<List<GroupResponse>> {
        val token = authManager.getJwtToken()
        return try {
            val response = apiService.getMyGroups("Bearer $token")
            Result.success(response.data ?: emptyList())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

