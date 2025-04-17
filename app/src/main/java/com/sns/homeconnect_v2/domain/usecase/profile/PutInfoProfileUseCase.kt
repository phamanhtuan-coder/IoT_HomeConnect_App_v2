package com.sns.homeconnect_v2.domain.usecase.profile

import com.sns.homeconnect_v2.data.remote.dto.request.UserRequest
import com.sns.homeconnect_v2.data.remote.dto.response.UserResponse
import com.sns.homeconnect_v2.domain.repository.UserRepository
import javax.inject.Inject


class PutInfoProfileUseCase @Inject constructor(
    private val userRepository: UserRepository
){
    suspend operator fun invoke (userId: Int, user: UserRequest): Result<UserResponse> {
        return try {
            val response = userRepository.putInfoProfile(userId, user)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }

    }
}