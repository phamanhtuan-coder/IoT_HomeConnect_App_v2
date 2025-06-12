package com.sns.homeconnect_v2.data.local.repository

import com.sns.homeconnect_v2.data.local.dao.UserGroupDao
import com.sns.homeconnect_v2.data.local.entity.UserGroupEntity
import jakarta.inject.Inject

class UserGroupLocalRepository @Inject constructor(
    private val dao: UserGroupDao
) {
    suspend fun getAllUserGroups() = dao.getAll()

    suspend fun saveUserGroups(userGroups: List<UserGroupEntity>) = dao.insertAll(userGroups)

    suspend fun clearUserGroups() = dao.clear()

    suspend fun getUserGroupsByAccountId(accountId: String) = dao.getUserGroupsByAccountId(accountId)

    suspend fun getUserGroupsByGroupId(groupId: Int) = dao.getUserGroupsByGroupId(groupId)
}
