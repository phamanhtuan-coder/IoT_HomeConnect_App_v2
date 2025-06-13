package com.sns.homeconnect_v2.data.local.repository

import com.sns.homeconnect_v2.data.local.dao.TemplateComponentDao
import com.sns.homeconnect_v2.data.local.entity.TemplateComponentEntity
import jakarta.inject.Inject

class TemplateComponentLocalRepository @Inject constructor(
    private val dao: TemplateComponentDao
) {
    suspend fun getAllActiveTemplateComponents() = dao.getAllActive()

    suspend fun getComponentsByTemplateId(templateId: String) = dao.getByTemplateId(templateId)

    suspend fun saveTemplateComponents(components: List<TemplateComponentEntity>) = dao.insertAll(components)

    suspend fun clearTemplateComponents() = dao.clear()
}
