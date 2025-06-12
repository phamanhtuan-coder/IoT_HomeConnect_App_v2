package com.sns.homeconnect_v2.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sns.homeconnect_v2.data.local.entity.UserGroupEntity

@Dao
interface UserGroupDao {
    @Query("SELECT * FROM user_groups")
    suspend fun getAll(): List<UserGroupEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(userGroups: List<UserGroupEntity>)

    @Query("DELETE FROM user_groups")
    suspend fun clear()

    @Query("SELECT * FROM user_groups WHERE account_id = :accountId AND is_deleted = 0")
    suspend fun getUserGroupsByAccountId(accountId: String): List<UserGroupEntity>

    @Query("SELECT * FROM user_groups WHERE group_id = :groupId AND is_deleted = 0")
    suspend fun getUserGroupsByGroupId(groupId: Int): List<UserGroupEntity>
}
