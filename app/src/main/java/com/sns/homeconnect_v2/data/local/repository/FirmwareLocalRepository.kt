package com.sns.homeconnect_v2.data.local.repository

import com.sns.homeconnect_v2.data.local.dao.FirmwareDao
import com.sns.homeconnect_v2.data.local.entity.FirmwareEntity
import jakarta.inject.Inject

class FirmwareLocalRepository @Inject constructor(
    private val dao: FirmwareDao
) {
    suspend fun getAllFirmwares() = dao.getAll()

    suspend fun saveFirmwares(firmwares: List<FirmwareEntity>) = dao.insertAll(firmwares)

    suspend fun clearFirmwares() = dao.clear()

    suspend fun getActiveFirmwares() = dao.getActiveFirmwares()

    suspend fun getFirmwaresByTemplateId(templateId: String) = dao.getFirmwaresByTemplateId(templateId)

    suspend fun getFirmwareById(firmwareId: String) = dao.getFirmwareById(firmwareId)

    suspend fun getMandatoryFirmwares() = dao.getMandatoryFirmwares()

    suspend fun getApprovedFirmwares() = dao.getApprovedFirmwares()
}
