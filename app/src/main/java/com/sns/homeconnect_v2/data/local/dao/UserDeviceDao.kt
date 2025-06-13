package com.sns.homeconnect_v2.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sns.homeconnect_v2.data.local.entity.UserDeviceEntity

@Dao
interface UserDeviceDao {

    @Query("SELECT * FROM user_devices")
    suspend fun getAll(): List<UserDeviceEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(devices: List<UserDeviceEntity>)

    @Query("DELETE FROM user_devices")
    suspend fun clear()
}
