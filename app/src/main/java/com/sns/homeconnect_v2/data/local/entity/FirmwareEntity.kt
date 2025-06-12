package com.sns.homeconnect_v2.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "firmware")
data class FirmwareEntity(
    @PrimaryKey
    @ColumnInfo(name = "firmware_id") val firmwareId: String,
    @ColumnInfo(name = "version") val version: String,
    @ColumnInfo(name = "file_path") val filePath: String,
    @ColumnInfo(name = "template_id") val templateId: String,
    @ColumnInfo(name = "is_mandatory") val isMandatory: Boolean,
    @ColumnInfo(name = "created_at") val createdAt: Long?,
    @ColumnInfo(name = "updated_at") val updatedAt: Long?,
    @ColumnInfo(name = "is_deleted") val isDeleted: Boolean,
    @ColumnInfo(name = "note") val note: String?,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "is_approved") val isApproved: Boolean,
    @ColumnInfo(name = "logs") val logs: String?,
    @ColumnInfo(name = "tested_at") val testedAt: Long?
)
