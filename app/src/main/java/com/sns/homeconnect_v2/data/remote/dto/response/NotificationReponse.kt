package com.sns.homeconnect_v2.data.remote.dto.response

import com.google.gson.annotations.SerializedName
import com.sns.homeconnect_v2.presentation.viewmodel.notification.AlertData

data class Notification(
    @SerializedName("id") val id: Int,
    @SerializedName("account_id") val accountId: String,
    @SerializedName("role_id") val roleId: String?,
    @SerializedName("text") val text: String,
    @SerializedName("type") val type: String,
    @SerializedName("is_read") val isRead: Boolean,
    @SerializedName("created_at") val createdAt: String,
    @SerializedName("updated_at") val updatedAt: String,
    @SerializedName("deleted_at") val deletedAt: String?
)

data class AlertResponse(
    val id: String,
    val error: String? = null,
    val data: AlertData? = null
)