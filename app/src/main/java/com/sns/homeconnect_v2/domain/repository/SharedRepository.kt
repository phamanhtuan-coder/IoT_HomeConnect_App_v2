package com.sns.homeconnect_v2.domain.repository

import com.sns.homeconnect_v2.data.remote.dto.response.SharedDeviceResponse
import com.sns.homeconnect_v2.data.remote.dto.response.SharedUser
import retrofit2.Response

interface SharedRepository {
    suspend fun getSharedDevicesForUser(): List<SharedDeviceResponse>

    suspend fun getSharedUsers(deviceId: Int): List<SharedUser>

    suspend fun addSharedUser(deviceId: Int, sharedWithUserEmail: String): Response<Unit>

    suspend fun revokePermission(permissionID: Int):   Response<Unit>

    suspend fun approveSharePermission(ticketId: String): Response<Unit>

    suspend fun revokeRecipientPermission(serialNumber: String): Response<Unit>
}