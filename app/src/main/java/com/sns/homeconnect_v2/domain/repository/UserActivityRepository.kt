package com.sns.homeconnect_v2.domain.repository

import com.sns.homeconnect_v2.data.remote.dto.response.UserActivityResponse

interface UserActivityRepository {
    suspend fun getUserActivities(): List<UserActivityResponse>
}