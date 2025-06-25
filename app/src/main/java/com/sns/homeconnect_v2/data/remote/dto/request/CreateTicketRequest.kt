package com.sns.homeconnect_v2.data.remote.dto.request

data class CreateTicketRequest(
    val title: String,
    val description: String,
    val ticket_type_id: Int,
    val device_serial: String,
    val evidence: Evidence
)

data class Evidence(
    val images: List<String>,
    val logs: List<String>
)
