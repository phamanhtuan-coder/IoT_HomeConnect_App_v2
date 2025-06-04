package com.sns.homeconnect_v2.domain.repository

import com.sns.homeconnect_v2.data.remote.dto.base.CreateGroupResponse
import com.sns.homeconnect_v2.data.remote.dto.request.CreateGroupRequest

interface GroupRepository {
    suspend fun createGroup(request: CreateGroupRequest): Result<CreateGroupResponse>
}