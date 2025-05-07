package com.sns.homeconnect_v2.data.remote.dto.base

data class MessageResponse(
    override val message: String,
    override val error: String?
) : IBaseResponse