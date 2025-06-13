package com.sns.homeconnect_v2.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "components")
data class ComponentEntity(
    @PrimaryKey
    @ColumnInfo(name = "component_id") val componentId: String,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "supplier") val supplier: String,
    @ColumnInfo(name = "unit_cost") val unitCost: Double,
    @ColumnInfo(name = "created_at") val createdAt: Long,
    @ColumnInfo(name = "updated_at") val updatedAt: Long,
    @ColumnInfo(name = "is_deleted") val isDeleted: Boolean,
    @ColumnInfo(name = "status") val status: Boolean
)
