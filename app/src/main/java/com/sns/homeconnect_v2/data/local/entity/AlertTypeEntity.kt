package com.sns.homeconnect_v2.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "alert_types")
data class AlertTypeEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "alert_type_id") val alertTypeId: Int,
    @ColumnInfo(name = "alert_type_name") val alertTypeName: String,
    @ColumnInfo(name = "priority") val priority: Int,
    @ColumnInfo(name = "is_deleted") val isDeleted: Boolean,
    @ColumnInfo(name = "created_at") val createdAt: Long,
    @ColumnInfo(name = "updated_at") val updatedAt: Long
)
