package com.sns.homeconnect_v2.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "production_tracking")
data class ProductionTrackingEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "production_id") val productionId: Int,
    @ColumnInfo(name = "device_serial") val deviceSerial: String,
    @ColumnInfo(name = "stage") val stage: String,
    @ColumnInfo(name = "status") val status: String,
    @ColumnInfo(name = "created_at") val createdAt: Long,
    @ColumnInfo(name = "updated_at") val updatedAt: Long,
    @ColumnInfo(name = "is_deleted") val isDeleted: Boolean,
    @ColumnInfo(name = "production_batch_id") val productionBatchId: String,
    @ColumnInfo(name = "state_logs") val stateLogs: String? // JSON string
)
