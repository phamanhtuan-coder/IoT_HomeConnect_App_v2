package com.sns.homeconnect_v2.data.remote.dto.response

data class User(
    val UserID: Int,
    val Name: String,
    val Email: String,
    val Phone: String,
    val Address: String,
    val DateOfBirth: String,
    val EmailVerified: Boolean,
    val ProfileImage: String
    // Todo: ... các trường khác nếu cần
)