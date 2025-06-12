package com.sns.homeconnect_v2.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sns.homeconnect_v2.data.local.entity.TicketEntity

@Dao
interface TicketDao {

    @Query("SELECT * FROM tickets WHERE is_deleted = 0")
    suspend fun getAllActive(): List<TicketEntity>

    @Query("SELECT * FROM tickets WHERE user_id = :userId AND is_deleted = 0")
    suspend fun getByUserId(userId: String): List<TicketEntity>

    @Query("SELECT * FROM tickets WHERE device_serial = :deviceSerial AND is_deleted = 0")
    suspend fun getByDeviceSerial(deviceSerial: String): List<TicketEntity>

    @Query("SELECT * FROM tickets WHERE ticket_id = :ticketId LIMIT 1")
    suspend fun getByTicketId(ticketId: String): TicketEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(tickets: List<TicketEntity>)

    @Query("DELETE FROM tickets")
    suspend fun clear()
}
