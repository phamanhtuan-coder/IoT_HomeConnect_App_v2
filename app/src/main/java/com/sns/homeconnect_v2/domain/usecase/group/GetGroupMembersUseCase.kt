package com.sns.homeconnect_v2.domain.usecase.group

import com.sns.homeconnect_v2.data.remote.dto.response.MemberResponse
import com.sns.homeconnect_v2.domain.repository.GroupRepository
import jakarta.inject.Inject

class GetGroupMembersUseCase @Inject constructor(
    private val groupRepository: GroupRepository
) {
    suspend operator fun invoke(groupId: Int): Result<List<MemberResponse>> {
        return try {
            val result = groupRepository.getGroupMembers(groupId)
            result
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
