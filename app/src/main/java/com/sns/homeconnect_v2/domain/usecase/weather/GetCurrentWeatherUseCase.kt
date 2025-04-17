package com.sns.homeconnect_v2.domain.usecase.weather

import com.sns.homeconnect_v2.data.remote.dto.response.WeatherResponse
import com.sns.homeconnect_v2.domain.repository.WeatherRepository
import javax.inject.Inject

class GetCurrentWeatherUseCase @Inject constructor(
    private val weatherRepository: WeatherRepository
) {
    suspend operator fun invoke(apiKey: String, location: String): Result<WeatherResponse> {
        return try {
            val response = weatherRepository.getCurrentWeather(apiKey, location)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}