package com.sns.homeconnect_v2.data.remote.dto.response

data class Space(
    val spaceId: Int,
    val houseId: Int,
    val createdAt: String,
    val updatedAt: String,
    val isDeleted: Boolean?,
    val spaceName: String,
    val iconColor: String?,
    val iconName: String?,
    val spaceDescription: String?,
    val isRevealed: Boolean = false
)