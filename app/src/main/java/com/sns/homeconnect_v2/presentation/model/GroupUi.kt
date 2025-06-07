package com.sns.homeconnect_v2.presentation.model

data class GroupUi(
    val id: Int,
    val name: String,
    val members: Int,
    val isRevealed: Boolean,
    val iconName: String,
    val iconColorName: String,
    val description: String? = null,
    val role: String = "member"
)
