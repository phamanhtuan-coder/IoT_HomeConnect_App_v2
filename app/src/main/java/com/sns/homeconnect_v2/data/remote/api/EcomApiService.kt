package com.sns.homeconnect_v2.data.remote.api

import com.sns.homeconnect_v2.data.remote.dto.response.CategoryDetailResponse
import com.sns.homeconnect_v2.data.remote.dto.response.ProductDetailResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface EcomApiService {
    @GET("product/detail/{productId}")
    suspend fun getProductDetail(@Path("productId") productId: Int): ProductDetailResponse

    @GET("categories/detail/{categoryId}")
    suspend fun getCategoryDetail(@Path("categoryId") categoryId: Int): CategoryDetailResponse
}