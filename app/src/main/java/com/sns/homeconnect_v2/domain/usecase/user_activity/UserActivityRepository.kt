package com.sns.homeconnect_v2.domain.usecase.user_activity

import com.sns.homeconnect_v2.data.remote.dto.response.UserActivityResponse
import com.sns.homeconnect_v2.domain.repository.UserActivityRepository
import jakarta.inject.Inject

class GetUserActivitiesUseCase @Inject constructor(
    private val repo: UserActivityRepository
) {
    suspend operator fun invoke(): List<UserActivityResponse> = repo.getUserActivities()
}
