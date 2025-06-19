package com.sns.homeconnect_v2.domain.repository

import com.sns.homeconnect_v2.data.remote.dto.response.CategoryData
import com.sns.homeconnect_v2.data.remote.dto.response.ProductData

interface EcomRepository {
    suspend fun getProductDetail(templateId: String): ProductData
    suspend fun getCategoryDetail(categoryId: Int): CategoryData
}