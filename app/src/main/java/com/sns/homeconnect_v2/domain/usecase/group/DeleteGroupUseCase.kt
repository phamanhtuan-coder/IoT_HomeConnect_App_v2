package com.sns.homeconnect_v2.domain.usecase.group

import android.util.Log
import com.sns.homeconnect_v2.domain.repository.GroupRepository
import jakarta.inject.Inject

class DeleteGroupUseCase @Inject constructor(
private val groupRepository: GroupRepository
) {
    suspend operator fun invoke(groupId: Int): Result<Unit> {
        return try {
            groupRepository.deleteGroupMember(groupId)
            Log.d("DeleteGroupUseCase", "Group deleted successfully")
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}