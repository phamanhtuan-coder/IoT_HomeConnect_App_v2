package com.sns.homeconnect_v2.domain.usecase.profile

import android.util.Log
import com.sns.homeconnect_v2.data.remote.dto.request.ChangePasswordRequest
import com.sns.homeconnect_v2.data.remote.dto.response.ChangePasswordResponse
import com.sns.homeconnect_v2.data.remote.dto.response.UserResponse
import com.sns.homeconnect_v2.domain.repository.UserRepository
import javax.inject.Inject

class UpdatePasswordUseCase @Inject constructor(
    private val userRepository: UserRepository
){
    suspend operator fun invoke (userId: Int, changePasswordRequest: ChangePasswordRequest): Result<ChangePasswordResponse> {
        return try {
            val response = userRepository.putChangePassword(userId, changePasswordRequest)
            Result.success(response)
        } catch (e: Exception) {
            Log.e("LoginViewModel", "Login error: ${e.message}")
            Result.failure(e)
        }

    }
}