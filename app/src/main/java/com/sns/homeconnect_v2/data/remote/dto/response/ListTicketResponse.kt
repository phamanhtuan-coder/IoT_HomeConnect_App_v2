package com.sns.homeconnect_v2.data.remote.dto.response

import Status
import com.google.gson.annotations.SerializedName
import com.sns.homeconnect_v2.presentation.model.TicketStatus

data class TicketResponse(
    @SerializedName("code") val code: Int,
    @SerializedName("message") val message: String,
    @SerializedName("status_code") val statusCode: Int,
    @SerializedName("data") val data: TicketData?
)

data class TicketData(
    @SerializedName("data") val tickets: List<Ticket>,
    @SerializedName("total_page") val totalPage: Int
)

data class Ticket(
    @SerializedName("ticket_id") val ticketId: String,
    @SerializedName("device_serial") val deviceSerial: String,
    @SerializedName("description") val description: String,
    @SerializedName("status") val status: String?,
    @SerializedName("assigned_to") val assignedTo: String?,
    @SerializedName("resolved_at") val resolvedAt: String?,
    @SerializedName("resolve_solution") val resolveSolution: String?,
    @SerializedName("is_deleted") val isDeleted: Int,
    @SerializedName("ticket_type_name") val ticketTypeName: String,
    @SerializedName("priority") val priority: Int,
    @SerializedName("user_name") val userName: String,
    @SerializedName("assigned_name") val assignedName: String,
    @SerializedName("created_at") val createdAt: String,
    @SerializedName("updated_at") val updatedAt: String,
    val IsViewed: Boolean = false,
)

data class TicketDetailResponse(
    @SerializedName("code") val code: Int,
    @SerializedName("message") val message: String,
    @SerializedName("status_code") val statusCode: Int,
    @SerializedName("data") val data: TicketDetailData?
)

data class TicketDetailData(
    @SerializedName("data") val ticket: List<TicketDetail>,
    @SerializedName("total_page") val totalPage: Int
)

data class TicketDetail(
    @SerializedName("ticket_id") val ticketId: String,
    @SerializedName("device_serial") val deviceSerial: String,
    @SerializedName("description") val description: String,
    @SerializedName("evidence") val evidence: Evidence?,
    @SerializedName("status") val status: String?,
    @SerializedName("assigned_to") val assignedTo: String?,
    @SerializedName("resolved_at") val resolvedAt: String?,
    @SerializedName("resolve_solution") val resolveSolution: String?,
    @SerializedName("ticket_type_name") val ticketTypeName: String,
    @SerializedName("priority") val priority: Int,
    @SerializedName("user_id") val userId: String,
    @SerializedName("user_name") val userName: String,
    @SerializedName("user_phone") val userPhone: String,
    @SerializedName("user_email") val userEmail: String,
    @SerializedName("assigned_name") val assignedName: String,
    @SerializedName("assigned_phone") val assignedPhone: String?,
    @SerializedName("assigned_email") val assignedEmail: String?,
    @SerializedName("device_name") val deviceName: String,
    @SerializedName("group_name") val groupName: String,
    @SerializedName("space_name") val spaceName: String,
    @SerializedName("template_name") val templateName: String,
    @SerializedName("house_name") val houseName: String,
    @SerializedName("created_at") val createdAt: String,
    @SerializedName("updated_at") val updatedAt: String,
    @SerializedName("is_deleted") val isDeleted: Int
)

data class Evidence(
    @SerializedName("logs") val logs: List<String>,
    @SerializedName("images") val images: List<String>
)