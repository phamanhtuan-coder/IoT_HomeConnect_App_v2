package com.sns.homeconnect_v2.domain.repository


import com.sns.homeconnect_v2.data.remote.dto.request.UpdateGroupRequest
import com.sns.homeconnect_v2.data.remote.dto.response.DeviceResponse
import com.sns.homeconnect_v2.data.remote.dto.response.DeviceResponseSpace
import com.sns.homeconnect_v2.data.remote.dto.response.SpaceResponse
import com.sns.homeconnect_v2.data.remote.dto.response.UpdateGroupResponse


interface SpaceRepository {
    suspend fun getSpacesByHomeId(homeId: Int): List<SpaceResponse>

    suspend fun getDevicesBySpaceId(spaceId: Int): List<DeviceResponseSpace>

//    suspend fun getSpaces(houseId: Int): List<SpaceResponse2>
//
suspend fun updateSpace(
    spaceId: Int,
    name: String,
    iconName: String? = null,
    iconColor: String? = null,
    description: String? = null
): Result<SpaceResponse>
//
//    suspend fun createSpace(houseId: Int, name: String): CreateSpaceResponse
//
//    suspend fun getSpaceDetail(spaceId: Int): SpaceDetailResponse
}