package com.sns.homeconnect_v2.data.remote.dto.request

data class ApproveRequest(
    var ticketId: String,
    var isApproved: Boolean = true
)