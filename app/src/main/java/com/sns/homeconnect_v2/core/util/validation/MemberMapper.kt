package com.sns.homeconnect_v2.core.util.validation

import com.sns.homeconnect_v2.data.remote.dto.response.MemberResponse
import com.sns.homeconnect_v2.presentation.model.MemberUi

fun MemberResponse.toUi(): MemberUi {
    return MemberUi(
        id = account_id,
        name = full_name ?: username,
        role = role,
        avatarUrl = avatar ?: "",
        isRevealed = false
    )
}
