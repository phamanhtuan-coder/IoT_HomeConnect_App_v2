package com.sns.homeconnect_v2.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ticket_types")
data class TicketTypeEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "ticket_type_id") val ticketTypeId: Int,
    @ColumnInfo(name = "type_name") val typeName: String,
    @ColumnInfo(name = "priority") val priority: Int,
    @ColumnInfo(name = "is_active") val isActive: Boolean,
    @ColumnInfo(name = "created_at") val createdAt: Long,
    @ColumnInfo(name = "updated_at") val updatedAt: Long,
    @ColumnInfo(name = "is_deleted") val isDeleted: Boolean
)
