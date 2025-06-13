package com.sns.homeconnect_v2.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sns.homeconnect_v2.data.local.entity.TemplateComponentEntity

@Dao
interface TemplateComponentDao {

    @Query("SELECT * FROM template_components WHERE is_deleted = 0")
    suspend fun getAllActive(): List<TemplateComponentEntity>

    @Query("SELECT * FROM template_components WHERE template_id = :templateId AND is_deleted = 0")
    suspend fun getByTemplateId(templateId: String): List<TemplateComponentEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(components: List<TemplateComponentEntity>)

    @Query("DELETE FROM template_components")
    suspend fun clear()
}
