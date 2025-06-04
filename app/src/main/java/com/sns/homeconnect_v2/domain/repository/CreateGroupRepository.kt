package com.sns.homeconnect_v2.domain.repository

import com.sns.homeconnect_v2.data.remote.dto.base.CreateGroupResponse
import com.sns.homeconnect_v2.data.remote.dto.request.CreateGroupRequest
import com.sns.homeconnect_v2.data.remote.dto.response.GroupResponse

interface GroupRepository {
    suspend fun createGroup(request: CreateGroupRequest): Result<CreateGroupResponse>
    suspend fun getMyGroups(): Result<List<GroupResponse>>
}
