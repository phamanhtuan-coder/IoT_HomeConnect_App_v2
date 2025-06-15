package com.sns.homeconnect_v2.domain.usecase.iot_device

import android.util.Log
import com.sns.homeconnect_v2.data.remote.dto.response.CategoryData
import com.sns.homeconnect_v2.data.remote.dto.response.ProductData
import com.sns.homeconnect_v2.domain.repository.EcomRepository
import jakarta.inject.Inject

data class DeviceDisplayInfo(
    val product: ProductData,
    val category: CategoryData
)

class GetDeviceDisplayInfoUseCase @Inject constructor(
    private val ecomRepository: EcomRepository
) {
    suspend operator fun invoke(templateId: Int): Result<DeviceDisplayInfo> {
        return try {
            val productId = templateId.toInt() ?: 1
            Log.d("CHECK", "Gọi getProductDetail với id: $productId (type: ${productId::class.simpleName})")
            val product = ecomRepository.getProductDetail(productId)
            Log.d("CHECK", "Product detail: $product")
            val category = ecomRepository.getCategoryDetail(product.category_id)
            Result.success(DeviceDisplayInfo(product, category))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

