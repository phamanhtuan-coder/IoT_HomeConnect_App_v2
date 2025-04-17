package com.sns.homeconnect_v2.domain.usecase.home

import com.sns.homeconnect_v2.data.remote.dto.response.SharedWithResponse
import com.sns.homeconnect_v2.domain.repository.UserRepository
import javax.inject.Inject

class FetchSharedWithUseCase  @Inject constructor(
    private val userRepository: UserRepository
){
    suspend operator fun invoke(userId: Int): Result<List<SharedWithResponse>> {
        return try {
            val response = userRepository.getSharedWith(userId)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}