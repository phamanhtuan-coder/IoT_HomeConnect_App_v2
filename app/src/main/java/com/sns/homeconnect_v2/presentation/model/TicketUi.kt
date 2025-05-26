package com.sns.homeconnect_v2.presentation.model

data class TicketUi(
    val id: Int,
    val nameUser: String,
    val typeTicket: String,
    val date: String,
    val content: String,
    val status: TicketStatus
)

enum class TicketStatus {
    PROCESSED, UNPROCESSED
}