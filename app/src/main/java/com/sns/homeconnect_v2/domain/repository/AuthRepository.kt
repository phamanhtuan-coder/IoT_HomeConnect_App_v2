package com.sns.homeconnect_v2.domain.repository

import com.sns.homeconnect_v2.data.remote.dto.request.RegisterRequest
import com.sns.homeconnect_v2.data.remote.dto.response.LoginResponse
import com.sns.homeconnect_v2.data.remote.dto.response.RegisterResponse
import com.sns.homeconnect_v2.data.remote.dto.response.User


interface AuthRepository {
    suspend fun login(email: String, password: String): LoginResponse
    suspend fun register(user: RegisterRequest): RegisterResponse
    suspend fun getInfoProfile(): User
}