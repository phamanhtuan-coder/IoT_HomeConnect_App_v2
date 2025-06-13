package com.sns.homeconnect_v2.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_groups")
data class UserGroupEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "user_group_id") val userGroupId: Int,
    @ColumnInfo(name = "account_id") val accountId: String,
    @ColumnInfo(name = "group_id") val groupId: Int,
    @ColumnInfo(name = "role") val role: String,
    @ColumnInfo(name = "joined_at") val joinedAt: Long?,
    @ColumnInfo(name = "updated_at") val updatedAt: Long?,
    @ColumnInfo(name = "is_deleted") val isDeleted: Boolean
)
