package com.sns.homeconnect_v2.data.local.repository

import com.sns.homeconnect_v2.data.local.dao.TicketTypeDao
import com.sns.homeconnect_v2.data.local.entity.TicketTypeEntity
import jakarta.inject.Inject

class TicketTypeLocalRepository @Inject constructor(
    private val dao: TicketTypeDao
) {
    suspend fun getAllActiveTicketTypes() = dao.getAllActive()

    suspend fun getTicketTypeById(id: Int) = dao.getById(id)

    suspend fun saveTicketTypes(types: List<TicketTypeEntity>) = dao.insertAll(types)

    suspend fun clearTicketTypes() = dao.clear()
}
