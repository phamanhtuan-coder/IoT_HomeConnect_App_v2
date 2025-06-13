package com.sns.homeconnect_v2.data.local.repository

import com.sns.homeconnect_v2.data.local.dao.HouseDao
import com.sns.homeconnect_v2.data.local.entity.HouseEntity
import jakarta.inject.Inject

class HouseLocalRepository @Inject constructor(
    private val dao: HouseDao
) {
    suspend fun getAllHouses() = dao.getAll()

    suspend fun saveHouses(houses: List<HouseEntity>) = dao.insertAll(houses)

    suspend fun clearHouses() = dao.clear()

    suspend fun getActiveHouses() = dao.getActiveHouses()

    suspend fun getHousesByGroupId(groupId: Int) = dao.getHousesByGroupId(groupId)

    suspend fun getHouseById(houseId: Int) = dao.getHouseById(houseId)
}
