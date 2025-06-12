package com.sns.homeconnect_v2.data.local.repository

import com.sns.homeconnect_v2.data.local.dao.TicketDao
import com.sns.homeconnect_v2.data.local.entity.TicketEntity
import jakarta.inject.Inject

class TicketLocalRepository @Inject constructor(
    private val dao: TicketDao
) {
    suspend fun getAllActiveTickets() = dao.getAllActive()

    suspend fun getTicketsByUserId(userId: String) = dao.getByUserId(userId)

    suspend fun getTicketsByDeviceSerial(deviceSerial: String) = dao.getByDeviceSerial(deviceSerial)

    suspend fun getTicketById(ticketId: String) = dao.getByTicketId(ticketId)

    suspend fun saveTickets(tickets: List<TicketEntity>) = dao.insertAll(tickets)

    suspend fun clearTickets() = dao.clear()
}
