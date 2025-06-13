package com.sns.homeconnect_v2.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "houses")
data class HouseEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "house_id") val houseId: Int,
    @ColumnInfo(name = "group_id") val groupId: Int,
    @ColumnInfo(name = "address") val address: String?,
    @ColumnInfo(name = "icon_name") val iconName: String?,
    @ColumnInfo(name = "icon_color") val iconColor: String?,
    @ColumnInfo(name = "created_at") val createdAt: Long?,
    @ColumnInfo(name = "updated_at") val updatedAt: Long?,
    @ColumnInfo(name = "is_deleted") val isDeleted: Boolean,
    @ColumnInfo(name = "house_name") val houseName: String
)
