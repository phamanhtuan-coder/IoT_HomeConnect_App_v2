package com.sns.homeconnect_v2.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "production_batches")
data class ProductionBatchEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "batch_id") val batchId: Int,
    @ColumnInfo(name = "production_batch_id") val productionBatchId: String,
    @ColumnInfo(name = "template_id") val templateId: String,
    @ColumnInfo(name = "quantity") val quantity: Int,
    @ColumnInfo(name = "status") val status: String,
    @ColumnInfo(name = "created_at") val createdAt: Long,
    @ColumnInfo(name = "updated_at") val updatedAt: Long,
    @ColumnInfo(name = "is_deleted") val isDeleted: Boolean,
    @ColumnInfo(name = "batch_note") val batchNote: String?,
    @ColumnInfo(name = "logs") val logs: String?,
    @ColumnInfo(name = "planning_id") val planningId: String,
    @ColumnInfo(name = "firmware_id") val firmwareId: String
)
