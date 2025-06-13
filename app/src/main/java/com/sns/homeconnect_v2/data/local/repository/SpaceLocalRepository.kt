package com.sns.homeconnect_v2.data.local.repository

import com.sns.homeconnect_v2.data.local.dao.SpaceDao
import com.sns.homeconnect_v2.data.local.entity.SpaceEntity
import jakarta.inject.Inject

class SpaceLocalRepository @Inject constructor(
    private val dao: SpaceDao
) {
    suspend fun getAllSpaces() = dao.getAll()

    suspend fun saveSpaces(spaces: List<SpaceEntity>) = dao.insertAll(spaces)

    suspend fun clearSpaces() = dao.clear()

    suspend fun getActiveSpaces() = dao.getActiveSpaces()

    suspend fun getSpacesByHouseId(houseId: Int) = dao.getSpacesByHouseId(houseId)

    suspend fun getSpaceById(spaceId: Int) = dao.getSpaceById(spaceId)
}
