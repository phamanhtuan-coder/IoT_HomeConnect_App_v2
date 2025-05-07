package com.sns.homeconnect_v2.data.remote.dto.response

data class SharedUser(
    val permissionId: Int,
    val deviceId: Int,
    val sharedWithUserId: Int,
    val sharedWithUser: SharedUserDetail,
    val createdAt: String,

    )

data class SharedUserDetail(
    val userId: Int,
    val name: String,
    val email: String
)

data class SharedUserRequest(
    val sharedWithUserEmail: String
)

data class RevokeShare(
    val permissionId: Int
)