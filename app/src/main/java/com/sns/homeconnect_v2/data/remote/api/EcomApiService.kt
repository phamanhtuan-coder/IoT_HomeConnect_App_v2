package com.sns.homeconnect_v2.data.remote.api

import com.sns.homeconnect_v2.data.remote.dto.response.CategoryDetailResponse
import com.sns.homeconnect_v2.data.remote.dto.response.ProductDetailResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface EcomApiService {
    @GET("/api/product/detail/{templateId}")
    suspend fun getProductDetail(@Path("templateId") templateId: String): ProductDetailResponse

    @GET("/api/categories/detail/{categoryId}")
    suspend fun getCategoryDetail(@Path("categoryId") categoryId: Int): CategoryDetailResponse
}