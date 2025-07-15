package com.sns.homeconnect_v2.data.remote.dto.response

/** Thống kê trung bình theo giờ */
data class HourlyValueResponse(
    val hourly_value_id: Int,
    val device_serial: String,
    val space_id: Int,
    val hour_timestamp: String,        // ISO-8601, UTC (Z)
    val avg_value: AvgValue,
    val sample_count: Int,
    val created_at: String,
    val updated_at: String,
    val is_deleted: Boolean
)

/** Giá trị trung bình từng cảm biến trong 1 giờ */
data class AvgValue(
    val gas: Double?,
    val humidity: Double?,
    val temperature: Double?,
    val power_consumption: Double?
)
