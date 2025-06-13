package com.sns.homeconnect_v2.data.local.repository

import com.sns.homeconnect_v2.data.local.dao.PlanningDao
import com.sns.homeconnect_v2.data.local.entity.PlanningEntity
import jakarta.inject.Inject

class PlanningLocalRepository @Inject constructor(
    private val dao: PlanningDao
) {
    suspend fun getAllActivePlans() = dao.getAllActive()

    suspend fun getPlanById(id: String) = dao.getById(id)

    suspend fun savePlans(plans: List<PlanningEntity>) = dao.insertAll(plans)

    suspend fun clearPlans() = dao.clear()
}
