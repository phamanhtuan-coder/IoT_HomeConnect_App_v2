package com.sns.homeconnect_v2.domain.usecase.group

import com.sns.homeconnect_v2.data.remote.dto.request.UpdateGroupRequest
import com.sns.homeconnect_v2.data.remote.dto.response.UpdateGroupResponse
import com.sns.homeconnect_v2.domain.repository.GroupRepository
import javax.inject.Inject

class UpdateGroupUseCase @Inject constructor(
    private val repository: GroupRepository
) {
    suspend operator fun invoke(groupId: Int, request: UpdateGroupRequest): Result<UpdateGroupResponse> {
        return try {
            val response = repository.updateGroup(groupId, request)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
