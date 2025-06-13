package com.sns.homeconnect_v2.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "hourly_values")
data class HourlyValueEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "hourly_value_id") val hourlyValueId: Int,
    @ColumnInfo(name = "device_serial") val deviceSerial: String,
    @ColumnInfo(name = "space_id") val spaceId: Int?,
    @ColumnInfo(name = "hour_timestamp") val hourTimestamp: Long?,
    @ColumnInfo(name = "avg_value") val avgValue: String?,
    @ColumnInfo(name = "sample_count") val sampleCount: Int?,
    @ColumnInfo(name = "created_at") val createdAt: Long?,
    @ColumnInfo(name = "updated_at") val updatedAt: Long?,
    @ColumnInfo(name = "is_deleted") val isDeleted: Boolean
)
