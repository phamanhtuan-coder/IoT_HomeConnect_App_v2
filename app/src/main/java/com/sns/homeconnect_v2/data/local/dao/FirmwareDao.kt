package com.sns.homeconnect_v2.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sns.homeconnect_v2.data.local.entity.FirmwareEntity

@Dao
interface FirmwareDao {
    @Query("SELECT * FROM firmware")
    suspend fun getAll(): List<FirmwareEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(firmwares: List<FirmwareEntity>)

    @Query("DELETE FROM firmware")
    suspend fun clear()

    @Query("SELECT * FROM firmware WHERE is_deleted = 0")
    suspend fun getActiveFirmwares(): List<FirmwareEntity>

    @Query("SELECT * FROM firmware WHERE template_id = :templateId AND is_deleted = 0")
    suspend fun getFirmwaresByTemplateId(templateId: String): List<FirmwareEntity>

    @Query("SELECT * FROM firmware WHERE firmware_id = :firmwareId AND is_deleted = 0")
    suspend fun getFirmwareById(firmwareId: String): FirmwareEntity?

    @Query("SELECT * FROM firmware WHERE is_mandatory = 1 AND is_deleted = 0")
    suspend fun getMandatoryFirmwares(): List<FirmwareEntity>

    @Query("SELECT * FROM firmware WHERE is_approved = 1 AND is_deleted = 0")
    suspend fun getApprovedFirmwares(): List<FirmwareEntity>
}
