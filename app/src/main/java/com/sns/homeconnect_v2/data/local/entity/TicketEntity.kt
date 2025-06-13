package com.sns.homeconnect_v2.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tickets")
data class TicketEntity(
    @PrimaryKey
    @ColumnInfo(name = "ticket_id") val ticketId: String,
    @ColumnInfo(name = "user_id") val userId: String,
    @ColumnInfo(name = "device_serial") val deviceSerial: String,
    @ColumnInfo(name = "ticket_type_id") val ticketTypeId: Int,
    @ColumnInfo(name = "description") val description: String?,
    @ColumnInfo(name = "evidence") val evidence: String?, // JSON string
    @ColumnInfo(name = "status") val status: String,
    @ColumnInfo(name = "created_at") val createdAt: Long,
    @ColumnInfo(name = "updated_at") val updatedAt: Long,
    @ColumnInfo(name = "assigned_to") val assignedTo: String?,
    @ColumnInfo(name = "resolved_at") val resolvedAt: Long?,
    @ColumnInfo(name = "resolve_solution") val resolveSolution: String?,
    @ColumnInfo(name = "is_deleted") val isDeleted: Boolean
)
