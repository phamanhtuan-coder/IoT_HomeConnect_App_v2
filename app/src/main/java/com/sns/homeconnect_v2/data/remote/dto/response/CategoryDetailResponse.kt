package com.sns.homeconnect_v2.data.remote.dto.response

data class CategoryDetailResponse(
    val status_code: Int,
    val data: CategoryData
)

data class CategoryData(
    val category_id: Int,
    val name: String,
    val slug: String?,
    val description: String?,
    val parent_id: Int?,
    val parent_name: String,
    val image: String?,
    val is_hide: Boolean?,
    val created_at: String?,
    val updated_at: String?,
    val deleted_at: String?
)