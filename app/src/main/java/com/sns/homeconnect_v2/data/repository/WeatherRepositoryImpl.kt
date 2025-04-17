package com.sns.homeconnect_v2.data.repository

import com.sns.homeconnect_v2.data.remote.api.WeatherApiService
import com.sns.homeconnect_v2.data.remote.dto.response.WeatherResponse
import com.sns.homeconnect_v2.domain.repository.WeatherRepository
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val weatherApiService: WeatherApiService
) : WeatherRepository {
    override suspend fun getCurrentWeather(apiKey: String, location: String): WeatherResponse {
        return weatherApiService.getCurrentWeather(apiKey, location)
    }
}