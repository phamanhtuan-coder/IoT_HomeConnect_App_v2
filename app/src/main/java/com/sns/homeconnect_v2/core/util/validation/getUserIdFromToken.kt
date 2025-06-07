package com.sns.homeconnect_v2.core.util.validation

import android.util.Base64
import org.json.JSONObject

fun getUserIdFromToken(token: String): String? {
    return try {
        val payload = token.split(".")[1]
        val decoded = Base64.decode(payload, Base64.URL_SAFE or Base64.NO_PADDING or Base64.NO_WRAP)
        val json = JSONObject(String(decoded, Charsets.UTF_8))
        json.optString("userId", null)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}