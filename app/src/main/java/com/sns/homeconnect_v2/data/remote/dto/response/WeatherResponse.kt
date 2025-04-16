package com.sns.homeconnect_v2.data.remote.dto.response

data class WeatherResponse(
    val location: Location,
    val current: Current
)

data class Location(
    val name: String,
    val region: String,
    val country: String,
    val localtime: String
)

data class Current(
    val tempC: Double,
    val condition: Condition,
    val windKph: Double,
    val humidity: Int,
    val visKm: Double
)

data class Condition(
    val text: String,
    val icon: String
)