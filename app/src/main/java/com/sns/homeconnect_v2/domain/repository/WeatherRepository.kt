package com.sns.homeconnect_v2.domain.repository

import com.sns.homeconnect_v2.data.remote.dto.response.WeatherResponse

interface WeatherRepository {
    suspend fun getCurrentWeather(apiKey: String, location: String): WeatherResponse
}