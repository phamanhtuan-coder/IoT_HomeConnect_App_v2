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
    private val repo: EcomRepository
) {
    suspend operator fun invoke(templateId: String): Result<DeviceDisplayInfo> = runCatching {
        val product = repo.getProductDetail(templateId)
        val category = repo.getCategoryDetail(product.category_id)
        DeviceDisplayInfo(product, category)
    }
}
