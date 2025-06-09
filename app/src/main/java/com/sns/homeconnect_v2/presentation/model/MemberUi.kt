package com.sns.homeconnect_v2.presentation.model

data class MemberUi(
    val id: String,
    val name: String,
    val role: String,
    val avatarUrl: String = "",
    val isRevealed: Boolean = false
)
