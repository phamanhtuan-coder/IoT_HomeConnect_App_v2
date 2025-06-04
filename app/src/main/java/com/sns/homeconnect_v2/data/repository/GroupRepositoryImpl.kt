package com.sns.homeconnect_v2.data.repository

import com.sns.homeconnect_v2.data.AuthManager
import com.sns.homeconnect_v2.data.remote.api.ApiService
import com.sns.homeconnect_v2.data.remote.dto.base.CreateGroupResponse
import com.sns.homeconnect_v2.data.remote.dto.request.CreateGroupRequest
import com.sns.homeconnect_v2.domain.repository.GroupRepository
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
}

