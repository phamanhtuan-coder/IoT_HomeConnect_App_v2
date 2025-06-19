package com.sns.homeconnect_v2.data.remote.dto.response

data class ProductDetailResponse(
    val status_code: Int,
    val data: ProductDataWrapper?
)

data class ProductDataWrapper(
    val data: List<ProductData>?,
    val total_page: Int?
)

data class ProductData(
    val id: String = "",
    val name: String? = null,
    val slug: String? = null,
    val description: String? = null,
    val description_normal: String? = null,
    val selling_price: Int? = null,
    val sold: Int? = null,
    val views: Int? = null,
    val status: Int? = null,
    val is_hide: Int? = null,
    val category_id: Int = 0,
    val categories: String? = null,
    val unit_id: Int? = null,
    val unit_name: String? = null,
    val stock: String? = null,
    val average_rating: String? = null,
    val total_liked: String? = null,
    val total_review: String? = null,
    val created_at: String? = null,
    val updated_at: String? = null,
    val deleted_at: String? = null,
    val reviews: List<Any>? = null,
    val images: List<Any>? = null,
    val image: String? = null,
    val warrenty_time_id: Int? = null,
    val specifications: List<Any>? = null
)

data class Specification(
    val id: Int = 0,
    val name: String? = null,
    val attributes: List<Attribute>? = null
)

data class Attribute(
    val id: Int = 0,
    val name: String? = null,
    val value: String? = null
)
