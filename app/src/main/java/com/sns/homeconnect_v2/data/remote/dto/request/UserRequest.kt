package com.sns.homeconnect_v2.data.remote.dto.request

data class UserRequest(
    val Name: String,
    val Email: String,
    val Phone: String,
    val Address: String,
    val DateOfBirth: String,
    val ProfileImage: String
)