package com.sns.homeconnect_v2.domain.repository

import com.sns.homeconnect_v2.data.remote.dto.request.ChangePasswordRequest
import com.sns.homeconnect_v2.data.remote.dto.request.UserRequest
import com.sns.homeconnect_v2.data.remote.dto.response.ChangePasswordResponse
import com.sns.homeconnect_v2.data.remote.dto.response.DeviceTokenResponse
import com.sns.homeconnect_v2.data.remote.dto.response.EmailResponse
import com.sns.homeconnect_v2.data.remote.dto.response.NewPasswordResponse
import com.sns.homeconnect_v2.data.remote.dto.response.SharedWithResponse
import com.sns.homeconnect_v2.data.remote.dto.response.UserResponse


interface UserRepository {
    suspend fun sendToken(deviceToken: String): DeviceTokenResponse
    suspend fun putChangePassword(userId: Int, request: ChangePasswordRequest): ChangePasswordResponse
    suspend fun confirmEmail(email: String): EmailResponse
    suspend fun getSharedWith(userId: Int): List<SharedWithResponse>
    suspend fun putInfoProfile(userId: Int, user: UserRequest): UserResponse
}