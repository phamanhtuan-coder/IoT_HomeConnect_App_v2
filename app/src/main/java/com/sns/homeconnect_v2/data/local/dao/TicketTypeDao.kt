package com.sns.homeconnect_v2.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sns.homeconnect_v2.data.local.entity.TicketTypeEntity

@Dao
interface TicketTypeDao {

    @Query("SELECT * FROM ticket_types WHERE is_deleted = 0 AND is_active = 1")
    suspend fun getAllActive(): List<TicketTypeEntity>

    @Query("SELECT * FROM ticket_types WHERE ticket_type_id = :id")
    suspend fun getById(id: Int): TicketTypeEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(types: List<TicketTypeEntity>)

    @Query("DELETE FROM ticket_types")
    suspend fun clear()
}
