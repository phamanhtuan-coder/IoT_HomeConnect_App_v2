package com.sns.homeconnect_v2.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ownership_history")
data class OwnershipHistoryEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "history_id") val historyId: Int,
    @ColumnInfo(name = "device_serial") val deviceSerial: String,
    @ColumnInfo(name = "from_user_id") val fromUserId: String,
    @ColumnInfo(name = "to_user_id") val toUserId: String,
    @ColumnInfo(name = "transferred_at") val transferredAt: Long,
    @ColumnInfo(name = "legal_expired_date") val legalExpiredDate: Long?,
    @ColumnInfo(name = "is_expired") val isExpired: Boolean,
    @ColumnInfo(name = "created_at") val createdAt: Long,
    @ColumnInfo(name = "updated_at") val updatedAt: Long,
    @ColumnInfo(name = "is_deleted") val isDeleted: Boolean,
    @ColumnInfo(name = "ticket_id") val ticketId: String
)
