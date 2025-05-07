package com.sns.homeconnect_v2.data.remote.dto.base

data class ApiResponse<T>(
    override val message: String,
    override val error: String? = null,
    val data: T? = null
) : IBaseResponse