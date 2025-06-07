package com.sns.homeconnect_v2.data.remote.dto.request

data class AddGroupMemberRequest(
    val groupId: Int,
    val accountId: String,
    val role: String
)
