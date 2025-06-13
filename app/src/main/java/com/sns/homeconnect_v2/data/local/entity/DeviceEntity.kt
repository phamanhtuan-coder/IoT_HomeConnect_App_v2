package com.sns.homeconnect_v2.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "devices")
data class DeviceEntity(
    @PrimaryKey
    @ColumnInfo(name = "device_id") val deviceId: String,
    @ColumnInfo(name = "serial_number") val serialNumber: String,
    @ColumnInfo(name = "template_id") val templateId: String,
    @ColumnInfo(name = "space_id") val spaceId: Int?,
    @ColumnInfo(name = "account_id") val accountId: String?,
    @ColumnInfo(name = "group_id") val groupId: Int?,
    @ColumnInfo(name = "hub_id") val hubId: String?,
    @ColumnInfo(name = "firmware_id") val firmwareId: String,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "power_status") val powerStatus: Boolean,
    @ColumnInfo(name = "attribute") val attribute: String?,
    @ColumnInfo(name = "wifi_ssid") val wifiSsid: String?,
    @ColumnInfo(name = "wifi_password") val wifiPassword: String?,
    @ColumnInfo(name = "current_value") val currentValue: String?,
    @ColumnInfo(name = "link_status") val linkStatus: String,
    @ColumnInfo(name = "last_reset_at") val lastResetAt: Long?,
    @ColumnInfo(name = "lock_status") val lockStatus: String,
    @ColumnInfo(name = "locked_at") val lockedAt: Long?,
    @ColumnInfo(name = "created_at") val createdAt: Long?,
    @ColumnInfo(name = "updated_at") val updatedAt: Long?,
    @ColumnInfo(name = "is_deleted") val isDeleted: Boolean,
    @ColumnInfo(name = "production_tracking_id") val productionTrackingId: Int?
)
