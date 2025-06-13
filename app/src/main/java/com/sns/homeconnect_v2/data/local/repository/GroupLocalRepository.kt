package com.sns.homeconnect_v2.data.local.repository

import com.sns.homeconnect_v2.data.local.dao.GroupDao
import com.sns.homeconnect_v2.data.local.entity.GroupEntity
import jakarta.inject.Inject

class GroupLocalRepository @Inject constructor(
    private val dao: GroupDao
) {
    suspend fun getAllGroups() = dao.getAll()

    suspend fun saveGroups(groups: List<GroupEntity>) = dao.insertAll(groups)

    suspend fun clearGroups() = dao.clear()

    suspend fun getActiveGroups() = dao.getActiveGroups()

    suspend fun getGroupById(groupId: Int) = dao.getGroupById(groupId)
}
