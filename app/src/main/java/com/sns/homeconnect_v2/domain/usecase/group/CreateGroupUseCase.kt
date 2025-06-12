package com.sns.homeconnect_v2.domain.usecase.group

import com.sns.homeconnect_v2.data.remote.dto.base.CreateGroupResponse
import com.sns.homeconnect_v2.data.remote.dto.request.CreateGroupRequest
import com.sns.homeconnect_v2.domain.repository.GroupRepository
import javax.inject.Inject

sealed class CreateGroupResult {
    data class Success(val message: String) : CreateGroupResult()
    data class Failure(val message: String) : CreateGroupResult()
}

class CreateGroupUseCase @Inject constructor(
    private val groupRepository: GroupRepository
) {
    suspend operator fun invoke(request: CreateGroupRequest): Result<String> {
        return try {
            groupRepository.createGroup(request)
            Result.success("Tạo nhóm thành công!")
        } catch (e: Exception) {
            Result.failure(Exception(e.message ?: "Tạo nhóm thất bại"))
        }
    }
}

