package com.sns.homeconnect_v2.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sync_tracking")
data class SyncTrackingEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "sync_id") val syncId: Int,
    @ColumnInfo(name = "account_id") val accountId: Int,
    @ColumnInfo(name = "user_device_id") val userDeviceId: Int,
    @ColumnInfo(name = "ip_address") val ipAddress: String?,
    @ColumnInfo(name = "last_synced_at") val lastSyncedAt: Long?,
    @ColumnInfo(name = "sync_type") val syncType: String?,
    @ColumnInfo(name = "sync_status") val syncStatus: String?,
    @ColumnInfo(name = "created_at") val createdAt: Long?,
    @ColumnInfo(name = "updated_at") val updatedAt: Long?,
)
