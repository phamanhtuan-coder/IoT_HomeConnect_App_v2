package com.sns.homeconnect_v2.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "device_templates")
data class DeviceTemplateEntity(
    @PrimaryKey
    @ColumnInfo(name = "template_id") val templateId: String,
    @ColumnInfo(name = "device_type_id") val deviceTypeId: Int,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "created_by") val createdBy: String,
    @ColumnInfo(name = "created_at") val createdAt: Long,
    @ColumnInfo(name = "updated_at") val updatedAt: Long,
    @ColumnInfo(name = "is_deleted") val isDeleted: Boolean,
    @ColumnInfo(name = "device_template_note") val deviceTemplateNote: String,
    @ColumnInfo(name = "status") val status: String,
    @ColumnInfo(name = "production_cost") val productionCost: Double
)
