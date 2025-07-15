package com.sns.homeconnect_v2.domain.usecase.iot_device.sharing

import com.sns.homeconnect_v2.domain.repository.SharedRepository
import javax.inject.Inject

sealed class RevokeResult {
    object Success : RevokeResult()
    data class Failure(val httpCode: Int, val message: String?) : RevokeResult()
}

class RevokeRecipientPermissionUseCase @Inject constructor(
    private val repo: SharedRepository
) {
    suspend operator fun invoke(serial: String): RevokeResult = try {
        val resp = repo.revokeRecipientPermission(serial)

        if (resp.isSuccessful) {
            RevokeResult.Success                      // 204
        } else {
            val rawMsg = resp.errorBody()?.string()   // chuỗi JSON thô (nếu cần)
            RevokeResult.Failure(resp.code(), rawMsg) // 404 / 5xx
        }
    } catch (e: Exception) {                          // lỗi mạng
        RevokeResult.Failure(-1, e.localizedMessage)
    }
}
