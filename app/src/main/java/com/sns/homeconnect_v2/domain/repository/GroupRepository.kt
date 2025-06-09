package com.sns.homeconnect_v2.domain.repository

import com.sns.homeconnect_v2.data.remote.dto.base.CreateGroupResponse
import com.sns.homeconnect_v2.data.remote.dto.request.AddGroupMemberRequest
import com.sns.homeconnect_v2.data.remote.dto.request.CreateGroupRequest
import com.sns.homeconnect_v2.data.remote.dto.request.UpdateGroupRequest
import com.sns.homeconnect_v2.data.remote.dto.response.GroupResponse
import com.sns.homeconnect_v2.data.remote.dto.response.MemberResponse
import com.sns.homeconnect_v2.data.remote.dto.response.UpdateGroupMemberRoleResponse
import com.sns.homeconnect_v2.data.remote.dto.response.UpdateGroupResponse
import com.sns.homeconnect_v2.data.remote.dto.response.UserGroupResponse

interface GroupRepository {
    suspend fun createGroup(request: CreateGroupRequest): Result<CreateGroupResponse>
    suspend fun getMyGroups(): Result<List<GroupResponse>>
    suspend fun updateGroup(groupId: Int, request: UpdateGroupRequest): UpdateGroupResponse
    suspend fun getGroupMembers(groupId: Int): Result<List<MemberResponse>>
    suspend fun addGroupMember(request: AddGroupMemberRequest): Result<UserGroupResponse>
    suspend fun updateGroupMemberRole(
        groupId: Int,
        accountId: String,
        role: String
    ): Result<UpdateGroupMemberRoleResponse>
}
