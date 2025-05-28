package com.sns.homeconnect_v2.presentation.model

import ChatMessageType

// Data class cho message để code gọn hơn:
data class ChatMessageUi(
    val message: String? = null,
    val imageUrl: String? = null,
    val time: String,
    val avatarUrl: String? = null,
    val type: ChatMessageType
)