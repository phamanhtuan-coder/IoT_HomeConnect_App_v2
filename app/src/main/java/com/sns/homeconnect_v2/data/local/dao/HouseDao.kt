package com.sns.homeconnect_v2.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sns.homeconnect_v2.data.local.entity.HouseEntity

@Dao
interface HouseDao {
    @Query("SELECT * FROM houses")
    suspend fun getAll(): List<HouseEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(houses: List<HouseEntity>)

    @Query("DELETE FROM houses")
    suspend fun clear()

    @Query("SELECT * FROM houses WHERE is_deleted = 0")
    suspend fun getActiveHouses(): List<HouseEntity>

    @Query("SELECT * FROM houses WHERE group_id = :groupId AND is_deleted = 0")
    suspend fun getHousesByGroupId(groupId: Int): List<HouseEntity>

    @Query("SELECT * FROM houses WHERE house_id = :houseId AND is_deleted = 0")
    suspend fun getHouseById(houseId: Int): HouseEntity?
}
