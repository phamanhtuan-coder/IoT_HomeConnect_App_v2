package com.sns.homeconnect_v2.domain.usecase.group

import com.sns.homeconnect_v2.data.remote.dto.base.CreateGroupResponse
import com.sns.homeconnect_v2.data.remote.dto.request.CreateGroupRequest
import com.sns.homeconnect_v2.domain.repository.GroupRepository
import javax.inject.Inject

class CreateGroupUseCase @Inject constructor(
    private val createGroupRepository: GroupRepository,
) {
    suspend operator fun invoke(group: CreateGroupRequest): Result<CreateGroupResponse> {
        return try {
            createGroupRepository.createGroup(group)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
