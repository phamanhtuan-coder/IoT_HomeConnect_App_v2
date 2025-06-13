package com.sns.homeconnect_v2.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "groups")
data class GroupEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "group_id") val groupId: Int,
    @ColumnInfo(name = "created_at") val createdAt: Long?,
    @ColumnInfo(name = "updated_at") val updatedAt: Long?,
    @ColumnInfo(name = "is_deleted") val isDeleted: Boolean,
    @ColumnInfo(name = "group_name") val groupName: String?,
    @ColumnInfo(name = "group_description") val groupDescription: String?,
    @ColumnInfo(name = "icon_color") val iconColor: String?,
    @ColumnInfo(name = "icon_name") val iconName: String?
)
