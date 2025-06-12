package com.sns.homeconnect_v2.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sns.homeconnect_v2.data.local.entity.GroupEntity

@Dao
interface GroupDao {
    @Query("SELECT * FROM `groups`")
    suspend fun getAll(): List<GroupEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(groups: List<GroupEntity>)

    @Query("DELETE FROM `groups`")
    suspend fun clear()

    @Query("SELECT * FROM `groups` WHERE is_deleted = 0")
    suspend fun getActiveGroups(): List<GroupEntity>

    @Query("SELECT * FROM `groups` WHERE group_id = :groupId")
    suspend fun getGroupById(groupId: Int): GroupEntity?
}
