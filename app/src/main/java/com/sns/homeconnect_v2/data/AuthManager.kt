package com.sns.homeconnect_v2.data

import android.content.Context
import com.sns.homeconnect_v2.core.util.validation.getUserIdFromToken
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class AuthManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val prefs = context.getSharedPreferences("MY_APP_PREFS", Context.MODE_PRIVATE)

    fun getJwtToken(): String = prefs.getString("JWT_TOKEN", "") ?: ""
    fun saveJwtToken(token: String) = prefs.edit().putString("JWT_TOKEN", token).apply()
    fun clearJwtToken() = prefs.edit().remove("JWT_TOKEN").apply()

    fun getRefreshToken(): String = prefs.getString("JWT_TOKEN_REFRESH", "") ?: ""
    fun saveRefreshToken(token: String) = prefs.edit().putString("JWT_TOKEN_REFRESH", token).apply()

    fun getDeviceUuid(): String = prefs.getString("JWT_TOKEN_DEVICE_UUID", "") ?: ""
    fun saveDeviceUuid(uuid: String) = prefs.edit().putString("JWT_TOKEN_DEVICE_UUID", uuid).apply()

    fun getCurrentUserId(): String {
        val token = getJwtToken()
        return getUserIdFromToken(token) ?: ""
    }

    // ✅ THÊM MỚI — dùng cho socket (accountId)
    fun saveAccountId(accountId: String) = prefs.edit().putString("ACCOUNT_ID", accountId).apply()
    fun getAccountId(): String = prefs.getString("ACCOUNT_ID", "") ?: ""
}


