package com.sns.homeconnect_v2.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sns.homeconnect_v2.data.local.entity.DeviceTemplateEntity

@Dao
interface DeviceTemplateDao {

    @Query("SELECT * FROM device_templates WHERE is_deleted = 0")
    suspend fun getAllActive(): List<DeviceTemplateEntity>

    @Query("SELECT * FROM device_templates WHERE template_id = :id")
    suspend fun getById(id: String): DeviceTemplateEntity?

    @Query("SELECT * FROM device_templates WHERE device_type_id = :typeId AND is_deleted = 0")
    suspend fun getByDeviceTypeId(typeId: Int): List<DeviceTemplateEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(templates: List<DeviceTemplateEntity>)

    @Query("DELETE FROM device_templates")
    suspend fun clear()
}
