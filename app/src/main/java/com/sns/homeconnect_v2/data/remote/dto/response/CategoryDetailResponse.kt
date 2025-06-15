package com.sns.homeconnect_v2.data.remote.dto.response

data class CategoryDetailResponse(
    val data: CategoryData
)

data class CategoryData(
    val category_id: Int,
    val name: String,
    val parent_id: Int?,
    val parent_name: String?
)
