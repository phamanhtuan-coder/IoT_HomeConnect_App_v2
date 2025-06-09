package com.sns.homeconnect_v2.domain.usecase.house

import android.util.Log
import com.sns.homeconnect_v2.data.remote.dto.request.CreateHouseRequest
import com.sns.homeconnect_v2.data.remote.dto.response.house.CreateHouseResponse
import com.sns.homeconnect_v2.domain.repository.HouseRepository
import javax.inject.Inject

class CreateHouseUseCase @Inject constructor(
    private val houseRepository: HouseRepository
) {
    suspend operator fun invoke(
        groupId: Int,
        houseName: String,
        address: String,
        iconName: String,
        iconColor: String
    ): Result<CreateHouseResponse> {
        return try {
            val request = CreateHouseRequest(
                groupId = groupId,
                house_name = houseName,
                address = address,
                icon_name = iconName,
                icon_color = iconColor
            )
            Log.d("CreateHouseUseCase", "Creating house with request: $request")
            val response = houseRepository.createHouse(request)
            Log.d("CreateHouseUseCase", "House created successfully: $response")
            Result.success(response)
        } catch (e: Exception) {
            Log.e("CreateHouseUseCase", "Error creating house: ${e.message}")
            Result.failure(e)
        }
    }
}