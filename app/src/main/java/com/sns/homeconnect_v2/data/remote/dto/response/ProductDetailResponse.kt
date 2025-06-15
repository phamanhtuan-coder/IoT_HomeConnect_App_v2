package com.sns.homeconnect_v2.data.remote.dto.response

data class ProductDetailResponse(
    val data: ProductDetailDataWrapper?
)

data class ProductDetailDataWrapper(
    val data: List<ProductData>?
)

data class ProductData(
    val id: Int = 0,
    val name: String? = null,
    val slug: String? = null,
    val description: String? = null,
    val description_normal: String? = null,
    val selling_price: Int = 0,
    val sold: Int = 0,
    val views: Int = 0,
    val status: Int = 0,
    val is_hide: Int = 0,
    val category_id: Int = 0,
    val categories: String? = null, // alias cho category_name
    val unit_id: Int? = null,
    val unit_name: String? = null,
    val stock: String? = null,
    val average_rating: String? = null,
    val total_liked: String? = null,
    val total_review: String? = null,
    val created_at: String? = null,
    val updated_at: String? = null,
    val deleted_at: String? = null,
    val image: String? = null,
    val warrenty_time_id: Int? = null,
    val reviews: List<Any>? = null,
    val images: List<String>? = null,
    val specifications: List<Specification>? = null
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
