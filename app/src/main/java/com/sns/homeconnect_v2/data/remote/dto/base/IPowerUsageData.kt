package com.sns.homeconnect_v2.data.remote.dto.base

interface IPowerUsageData {
    val energyConsumed: Double
    val powerRating: Double
    val totalOnTimeHours: Double
}