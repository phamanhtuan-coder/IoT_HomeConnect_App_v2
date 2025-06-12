package com.sns.homeconnect_v2.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "planning")
data class PlanningEntity(
    @PrimaryKey
    @ColumnInfo(name = "planning_id") val planningId: String,
    @ColumnInfo(name = "status") val status: String,
    @ColumnInfo(name = "logs") val logs: String?, // dạng JSON string
    @ColumnInfo(name = "created_by") val createdBy: String,
    @ColumnInfo(name = "created_at") val createdAt: Long,
    @ColumnInfo(name = "updated_at") val updatedAt: Long,
    @ColumnInfo(name = "is_deleted") val isDeleted: Boolean,
    @ColumnInfo(name = "planning_note") val planningNote: String?
)
