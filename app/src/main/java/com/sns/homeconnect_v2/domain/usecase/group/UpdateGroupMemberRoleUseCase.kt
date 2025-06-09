package com.sns.homeconnect_v2.domain.usecase.group

import com.sns.homeconnect_v2.domain.repository.GroupRepository
import javax.inject.Inject

class UpdateGroupMemberRoleUseCase @Inject constructor(
    private val groupRepository: GroupRepository
) {
    suspend operator fun invoke(
        groupId: Int,
        accountId: String,
        role: String
    ): Result<Boolean> {
        return groupRepository.updateGroupMemberRole(groupId, accountId, role).map { true }
    }
}
