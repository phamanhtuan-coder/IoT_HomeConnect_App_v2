package com.sns.homeconnect_v2.domain.usecase.group

import com.sns.homeconnect_v2.data.remote.dto.response.GroupResponse
import com.sns.homeconnect_v2.domain.repository.GroupRepository
import jakarta.inject.Inject

class GetMyGroupsUseCase @Inject constructor(
    private val repository: GroupRepository
) {
    suspend operator fun invoke(): Result<List<GroupResponse>> {
        return try {
            repository.getMyGroups()
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

