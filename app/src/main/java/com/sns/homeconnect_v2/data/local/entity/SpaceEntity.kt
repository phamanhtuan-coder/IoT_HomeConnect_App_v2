package com.sns.homeconnect_v2.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "spaces")
data class SpaceEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "space_id") val spaceId: Int,
    @ColumnInfo(name = "house_id") val houseId: Int,
    @ColumnInfo(name = "created_at") val createdAt: Long?,
    @ColumnInfo(name = "updated_at") val updatedAt: Long?,
    @ColumnInfo(name = "is_deleted") val isDeleted: Boolean,
    @ColumnInfo(name = "space_name") val spaceName: String,
    @ColumnInfo(name = "icon_color") val iconColor: String?,
    @ColumnInfo(name = "icon_name") val iconName: String?,
    @ColumnInfo(name = "space_description") val spaceDescription: String?
)
