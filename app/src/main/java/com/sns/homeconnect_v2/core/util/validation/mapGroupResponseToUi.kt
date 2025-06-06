package com.sns.homeconnect_v2.core.util.validation

import com.sns.homeconnect_v2.data.AuthManager
import com.sns.homeconnect_v2.data.remote.dto.response.GroupResponse
import com.sns.homeconnect_v2.domain.usecase.group.GetGroupMembersUseCase
import com.sns.homeconnect_v2.presentation.model.GroupUi

suspend fun mapGroupResponseToUi(
    group: GroupResponse,
    memberUseCase: GetGroupMembersUseCase,
    authManager: AuthManager // cần thêm vào để lấy token
): GroupUi {
    val token = authManager.getJwtToken()
    val currentUserId = getUserIdFromToken(token)

    val membersResult = memberUseCase(group.group_id)
    val members = membersResult.getOrElse {
        println("Lỗi khi lấy member cho group ${group.group_id}: ${it.message}")
        emptyList()
    }

    val role = members.find { it.account_id == currentUserId }?.role ?: "member"

    println("Group ${group.group_name} có ${members.size} thành viên – Role người dùng: $role")

    return GroupUi(
        id = group.group_id,
        name = group.group_name,
        members = members.size,
        isRevealed = false,
        iconName = group.icon_name ?: "home",
        iconColorName = group.icon_color ?: "gray",
        description = group.group_description,
        role = role
    )
}

