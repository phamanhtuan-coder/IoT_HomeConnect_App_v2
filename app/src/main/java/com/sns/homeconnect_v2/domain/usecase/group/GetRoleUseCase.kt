package com.sns.homeconnect_v2.domain.usecase.group

import com.sns.homeconnect_v2.data.remote.dto.response.RoleResponse
import com.sns.homeconnect_v2.domain.repository.GroupRepository
import javax.inject.Inject

class GetRoleUseCase @Inject constructor(
    private val groupRepository: GroupRepository
) {
    suspend operator fun invoke(groupId: Int): Result<RoleResponse> {
        return try {
            groupRepository.getRole(groupId)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}