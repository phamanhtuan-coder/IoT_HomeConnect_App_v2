package com.sns.homeconnect_v2.domain.usecase.iot_device.sharing

import com.sns.homeconnect_v2.domain.repository.SharedRepository
import retrofit2.Response
import javax.inject.Inject

class RevokePermissionUseCase @Inject constructor(
    private val shareRepository: SharedRepository
) {
    suspend operator fun invoke(
        permissionID: Int
    ): Result<Response<Unit>> {
        return try {
            val response = shareRepository.revokePermission(permissionID)
             Result.success(response)
        } catch (e: Exception) {
          Result.failure(e)
        }
    }
}