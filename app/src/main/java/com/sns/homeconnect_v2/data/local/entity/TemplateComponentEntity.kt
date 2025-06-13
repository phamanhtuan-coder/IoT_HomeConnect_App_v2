package com.sns.homeconnect_v2.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "template_components")
data class TemplateComponentEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "template_component_id") val templateComponentId: Int,
    @ColumnInfo(name = "template_id") val templateId: String,
    @ColumnInfo(name = "component_id") val componentId: String,
    @ColumnInfo(name = "quantity_required") val quantityRequired: Int,
    @ColumnInfo(name = "created_at") val createdAt: Long,
    @ColumnInfo(name = "updated_at") val updatedAt: Long,
    @ColumnInfo(name = "is_deleted") val isDeleted: Boolean
)
