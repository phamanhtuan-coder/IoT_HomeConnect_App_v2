package com.sns.homeconnect_v2.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_devices")
data class UserDeviceEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "user_device_id") val userDeviceId: Int,
    @ColumnInfo(name = "user_id") val userId: Int,
    @ColumnInfo(name = "device_name") val deviceName: String,
    @ColumnInfo(name = "device_id") val deviceId: String,
    @ColumnInfo(name = "device_uuid") val deviceUuid: String?,
    @ColumnInfo(name = "device_token") val deviceToken: String?,
    @ColumnInfo(name = "last_login") val lastLogin: Long?,
    @ColumnInfo(name = "last_out") val lastOut: Long?,
    @ColumnInfo(name = "created_at") val createdAt: Long?,
    @ColumnInfo(name = "updated_at") val updatedAt: Long?,
)
