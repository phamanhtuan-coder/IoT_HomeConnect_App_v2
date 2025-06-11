package com.sns.homeconnect_v2.domain.usecase.group

import com.sns.homeconnect_v2.data.remote.dto.request.AddGroupMemberRequest
import com.sns.homeconnect_v2.data.remote.dto.response.UserGroupResponse
import com.sns.homeconnect_v2.domain.repository.GroupRepository
import javax.inject.Inject

class AddGroupMemberUseCase @Inject constructor(
    private val repository: GroupRepository
) {
    suspend operator fun invoke(groupId: Int, accountId: String, role: String): Result<UserGroupResponse> {
        return try {
            val request = AddGroupMemberRequest(
                groupId = groupId,
                accountId = accountId,
                role = role.lowercase()
            )
            repository.addGroupMember(request)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
