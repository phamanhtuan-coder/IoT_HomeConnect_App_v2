package com.sns.homeconnect_v2.data.local.repository

import com.sns.homeconnect_v2.data.local.dao.ComponentDao
import com.sns.homeconnect_v2.data.local.entity.ComponentEntity
import jakarta.inject.Inject

class ComponentLocalRepository @Inject constructor(
    private val dao: ComponentDao
) {
    suspend fun getAllActiveComponents() = dao.getAllActive()

    suspend fun getComponentById(id: String) = dao.getById(id)

    suspend fun saveComponents(components: List<ComponentEntity>) = dao.insertAll(components)

    suspend fun clearComponents() = dao.clear()
}
