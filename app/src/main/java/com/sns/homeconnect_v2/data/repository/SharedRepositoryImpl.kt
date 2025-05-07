package com.example.ungdungquanlyvadieukhieniot_homeconnect.data.repository

import android.content.Context
import com.example.ungdungquanlyvadieukhieniot_homeconnect.data.remote.api.RetrofitClient
import com.example.ungdungquanlyvadieukhieniot_homeconnect.data.remote.dto.SharedUser
import com.example.ungdungquanlyvadieukhieniot_homeconnect.data.remote.dto.SharedUserRequest
import retrofit2.Response

class SharedRepository(private val context: Context) {
    private val apiService = RetrofitClient.apiService

    suspend fun getSharedUsers(DeviceID: Int): List<SharedUser> {
        val sharedPrefs = context.getSharedPreferences("MY_APP_PREFS", Context.MODE_PRIVATE)
        val token = sharedPrefs.getString("JWT_TOKEN", "") ?: ""
        return apiService.getSharedUsers(DeviceID, token = "Bearer $token")
    }

    suspend fun addSharedUser(DeviceID: Int, SharedWithUserEmail: String): Response<Unit> {
        val sharedPrefs = context.getSharedPreferences("MY_APP_PREFS", Context.MODE_PRIVATE)
        val token = sharedPrefs.getString("JWT_TOKEN", "") ?: ""
        val request = SharedUserRequest(SharedWithUserEmail)
        return apiService.shareDevice(DeviceID, request, token = "Bearer $token")
    }

    suspend fun revokePermission(permissionID: Int): Response<Unit> {
        val sharedPrefs = context.getSharedPreferences("MY_APP_PREFS", Context.MODE_PRIVATE)
        val token = sharedPrefs.getString("JWT_TOKEN", "") ?: ""
        return apiService.revokePermission(permissionId = permissionID, token = "Bearer $token")
    }
}