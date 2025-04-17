package com.sns.homeconnect_v2.domain.repository

import com.sns.homeconnect_v2.data.remote.dto.response.DeviceTokenResponse
import com.sns.homeconnect_v2.data.remote.dto.response.EmailResponse


interface UserRepository {
//    suspend fun newPassword(email: String, password: String): NewPasswordResponse
    suspend fun sendToken(deviceToken: String): DeviceTokenResponse
//    suspend fun putInfoProfile(userId: Int, user: UserRequest): UserResponse
//    suspend fun putChangePassword(userId: Int, request: ChangePasswordRequest): ChangePasswordResponse
//    suspend fun getSharedWith(userId: Int): List<SharedWithResponse>
    suspend fun confirmEmail(email: String): EmailResponse
}