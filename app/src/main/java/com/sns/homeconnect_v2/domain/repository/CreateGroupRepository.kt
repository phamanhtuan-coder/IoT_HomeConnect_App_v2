package com.sns.homeconnect_v2.domain.repository

import com.sns.homeconnect_v2.data.remote.dto.base.CreateGroupResponse
import com.sns.homeconnect_v2.data.remote.dto.request.CreateGroupRequest
import com.sns.homeconnect_v2.data.remote.dto.response.GroupResponse
import com.sns.homeconnect_v2.data.remote.dto.request.UpdateGroupRequest
import com.sns.homeconnect_v2.data.remote.dto.response.UpdateGroupResponse

interface GroupRepository {
    suspend fun createGroup(request: CreateGroupRequest): Result<CreateGroupResponse>
    suspend fun getMyGroups(): Result<List<GroupResponse>>
    suspend fun updateGroup(groupId: Int, request: UpdateGroupRequest): UpdateGroupResponse
}
