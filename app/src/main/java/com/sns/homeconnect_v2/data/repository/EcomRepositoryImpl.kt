package com.sns.homeconnect_v2.data.repository

import android.util.Log
import com.sns.homeconnect_v2.data.remote.api.EcomApiService
import com.sns.homeconnect_v2.data.remote.dto.response.CategoryData
import com.sns.homeconnect_v2.data.remote.dto.response.ProductData
import com.sns.homeconnect_v2.domain.repository.EcomRepository
import jakarta.inject.Inject
import javax.inject.Named

class EcomRepositoryImpl @Inject constructor(
    @Named("HomeConnectEcomUrl") private val api: EcomApiService
): EcomRepository {

    override suspend fun getProductDetail(templateId: String): ProductData {
        val res = api.getProductDetail(templateId)
        Log.d("EcomRepository", "ProductDetailResponse: $res")
        return res.data?.data?.firstOrNull() ?: throw Exception("Product not found for id=$templateId")
    }

    override suspend fun getCategoryDetail(categoryId: Int): CategoryData {
        val res = api.getCategoryDetail(categoryId)
        Log.d("EcomRepository", "CategoryDetailResponse: $res")
        return res.data
    }
}
