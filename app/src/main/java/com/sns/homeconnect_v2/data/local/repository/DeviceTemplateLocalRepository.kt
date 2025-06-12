package com.sns.homeconnect_v2.data.local.repository

import com.sns.homeconnect_v2.data.local.dao.DeviceTemplateDao
import com.sns.homeconnect_v2.data.local.entity.DeviceTemplateEntity
import jakarta.inject.Inject

class DeviceTemplateLocalRepository @Inject constructor(
    private val dao: DeviceTemplateDao
) {
    suspend fun getAllActiveTemplates() = dao.getAllActive()

    suspend fun getTemplateById(id: String) = dao.getById(id)

    suspend fun getTemplatesByTypeId(typeId: Int) = dao.getByDeviceTypeId(typeId)

    suspend fun saveTemplates(templates: List<DeviceTemplateEntity>) = dao.insertAll(templates)

    suspend fun clearTemplates() = dao.clear()
}
