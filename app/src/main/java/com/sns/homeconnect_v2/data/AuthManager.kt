package com.sns.homeconnect_v2.data

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton
import androidx.core.content.edit

@Singleton
class AuthManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    fun getJwtToken(): String {
        val sharedPrefs = context.getSharedPreferences("MY_APP_PREFS", Context.MODE_PRIVATE)
        return sharedPrefs.getString("JWT_TOKEN", "") ?: ""
    }

    fun saveJwtToken(token: String) {
        val sharedPrefs = context.getSharedPreferences("MY_APP_PREFS", Context.MODE_PRIVATE)
        sharedPrefs.edit { putString("JWT_TOKEN", token) }
    }
}