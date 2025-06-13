package com.sns.homeconnect_v2.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "firmware_update_history")
data class FirmwareUpdateHistoryEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "update_id") val updateId: Int,
    @ColumnInfo(name = "device_serial") val deviceSerial: String,
    @ColumnInfo(name = "firmware_id") val firmwareId: String,
    @ColumnInfo(name = "updated_at") val updatedAt: Long,
    @ColumnInfo(name = "status") val status: String,
    @ColumnInfo(name = "created_at") val createdAt: Long,
    @ColumnInfo(name = "is_deleted") val isDeleted: Boolean
)

