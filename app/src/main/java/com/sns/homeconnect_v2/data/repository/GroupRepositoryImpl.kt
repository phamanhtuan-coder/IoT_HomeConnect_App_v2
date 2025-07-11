package com.sns.homeconnect_v2.data.repository

import com.sns.homeconnect_v2.data.AuthManager
import com.sns.homeconnect_v2.data.remote.api.ApiService
import com.sns.homeconnect_v2.data.remote.dto.base.CreateGroupResponse
import com.sns.homeconnect_v2.data.remote.dto.request.AddGroupMemberRequest
import com.sns.homeconnect_v2.data.remote.dto.request.CreateGroupRequest
import com.sns.homeconnect_v2.data.remote.dto.request.UpdateGroupMemberRoleRequest
import com.sns.homeconnect_v2.data.remote.dto.request.UpdateGroupRequest
import com.sns.homeconnect_v2.data.remote.dto.response.GroupResponse
import com.sns.homeconnect_v2.data.remote.dto.response.MemberResponse
import com.sns.homeconnect_v2.data.remote.dto.response.RoleResponse
import com.sns.homeconnect_v2.data.remote.dto.response.UpdateGroupMemberRoleResponse
import com.sns.homeconnect_v2.data.remote.dto.response.UpdateGroupResponse
import com.sns.homeconnect_v2.data.remote.dto.response.UserGroupResponse
import com.sns.homeconnect_v2.data.remote.dto.response.house.House
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

    override suspend fun updateGroup(groupId: Int, request: UpdateGroupRequest): UpdateGroupResponse {
        val token = authManager.getJwtToken()
        return apiService.updateGroup(groupId, request, "Bearer $token")
    }

    override suspend fun getMyGroups(): Result<List<GroupResponse>> {
        val token = authManager.getJwtToken()
        return try {
            val response = apiService.getMyGroups(
                page = 1,
                limit = 10,
                token = "Bearer $token"
            )
            Result.success(response.data)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getGroupMembers(groupId: Int): Result<List<MemberResponse>> {
        val token = authManager.getJwtToken()
        return try {
            val response = apiService.getGroupMembers(groupId, "Bearer $token")
            Result.success(response.data ?: emptyList())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun addGroupMember(request: AddGroupMemberRequest): Result<UserGroupResponse> {
        val token = authManager.getJwtToken()
        return try {
            val response = apiService.addGroupMember(request, "Bearer $token")
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateGroupMemberRole(
        groupId: Int,
        accountId: String,
        role: String
    ): Result<UpdateGroupMemberRoleResponse> = runCatching {
        val token = authManager.getJwtToken()
        val request = UpdateGroupMemberRoleRequest(accountId, role)
        apiService.updateGroupMemberRole(groupId, request, "Bearer $token")
    }

    override suspend fun deleteGroupMember(groupId: Int): Result<Unit> {
        val token = authManager.getJwtToken()
        return try {
            apiService.deleteGroup(groupId, "Bearer $token")
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getListHouseByGroup(groupId: Int): Result<List<House>> {
        val token = authManager.getJwtToken()
        return try {
            val response = apiService.getHousesByGroup(groupId, token = "Bearer $token")
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getRole(groupId: Int): Result<RoleResponse> {
        val token = authManager.getJwtToken()
        return try {
            val response = apiService.getRole(groupId, "Bearer $token")
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
