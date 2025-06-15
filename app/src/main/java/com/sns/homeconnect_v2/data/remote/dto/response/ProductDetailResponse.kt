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
    val category_id: Int = 0,
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
