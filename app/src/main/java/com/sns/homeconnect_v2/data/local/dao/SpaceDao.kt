package com.sns.homeconnect_v2.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sns.homeconnect_v2.data.local.entity.SpaceEntity

@Dao
interface SpaceDao {
    @Query("SELECT * FROM spaces")
    suspend fun getAll(): List<SpaceEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(spaces: List<SpaceEntity>)

    @Query("DELETE FROM spaces")
    suspend fun clear()

    @Query("SELECT * FROM spaces WHERE is_deleted = 0")
    suspend fun getActiveSpaces(): List<SpaceEntity>

    @Query("SELECT * FROM spaces WHERE house_id = :houseId AND is_deleted = 0")
    suspend fun getSpacesByHouseId(houseId: Int): List<SpaceEntity>

    @Query("SELECT * FROM spaces WHERE space_id = :spaceId AND is_deleted = 0")
    suspend fun getSpaceById(spaceId: Int): SpaceEntity?
}
