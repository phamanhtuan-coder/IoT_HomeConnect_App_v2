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
) : EcomRepository {
    override suspend fun getProductDetail(productId: Int): ProductData {
        // Giả sử đã sửa data class đúng ở trên
        val response = api.getProductDetail(productId)
        Log.d("CHECK", "API raw response: $response")
        val product = response.data?.data?.firstOrNull()
        Log.d("CHECK", "Product detail: $product")
        return response.data?.data?.firstOrNull() ?: ProductData()
    }

    override suspend fun getCategoryDetail(categoryId: Int): CategoryData {
        return api.getCategoryDetail(categoryId).data
    }
}
