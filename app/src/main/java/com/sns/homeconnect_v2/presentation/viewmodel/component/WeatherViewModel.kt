package com.sns.homeconnect_v2.presentation.viewmodel.component

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.sns.homeconnect_v2.core.permission.PermissionManager
import com.sns.homeconnect_v2.data.remote.dto.response.WeatherResponse
import com.sns.homeconnect_v2.domain.usecase.weather.GetCurrentWeatherUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.sns.homeconnect_v2.BuildConfig

sealed class WeatherState {
    data object Idle : WeatherState()
    data object Loading : WeatherState()
    data class Success(val weatherResponse: WeatherResponse) : WeatherState()
    data class Error(val message: String) : WeatherState()
    data object PermissionRequired : WeatherState()
}

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val getCurrentWeatherUseCase: GetCurrentWeatherUseCase,
    private val permissionManager: PermissionManager
) : ViewModel() {
    private val _weatherState = MutableStateFlow<WeatherState>(WeatherState.Idle)
    val weatherState = _weatherState.asStateFlow()
    private val apiKey = BuildConfig.WEATHER_API_KEY

    fun fetchWeather(context: Context, location: String = "Hanoi") {
        viewModelScope.launch {
            _weatherState.value = WeatherState.Loading
            if (!hasLocationPermission()) {
                _weatherState.value = WeatherState.PermissionRequired
            } else {
                fetchWeatherWithLocation(context, location)
            }
        }
    }

    fun onPermissionGranted(context: Context, location: String = "Hanoi") {
        viewModelScope.launch {
            fetchWeatherWithLocation(context, location)
        }
    }

    fun onPermissionDenied() {
        viewModelScope.launch {
            _weatherState.value = WeatherState.Error("Location permission denied")
        }
    }

    private fun fetchWeatherWithLocation(context: Context, defaultLocation: String) {
        getCurrentLocation(context) { loc ->
            viewModelScope.launch {
                val targetLocation = if (loc != "Hanoi") loc else defaultLocation
                getCurrentWeatherUseCase(apiKey, targetLocation).fold(
                    onSuccess = { response ->
                        _weatherState.value = WeatherState.Success(response)
                    },
                    onFailure = { e ->
                        _weatherState.value =
                            WeatherState.Error(e.message ?: "Failed to fetch weather")
                        Log.e("WeatherViewModel", "Error: ${e.message}")
                    }
                )
            }
        }
    }

    private fun getCurrentLocation(context: Context, onLocationResult: (String) -> Unit) {
        // Check permissions explicitly
        if (!hasLocationPermission()) {
            Log.w("WeatherViewModel", "Location permissions not granted")
            onLocationResult("Hanoi")
            return
        }

        try {
            val fusedLocationClient: FusedLocationProviderClient =
                LocationServices.getFusedLocationProviderClient(context)

            fusedLocationClient.lastLocation
                .addOnSuccessListener { location ->
                    if (location != null) {
                        val latitude = location.latitude
                        val longitude = location.longitude
                        onLocationResult("$latitude,$longitude")
                    } else {
                        Log.w("WeatherViewModel", "Location is null")
                        onLocationResult("Hanoi")
                    }
                }
                .addOnFailureListener { e ->
                    Log.e("WeatherViewModel", "Failed to fetch location: ${e.message}")
                    onLocationResult("Hanoi")
                }
        } catch (e: SecurityException) {
            Log.e("WeatherViewModel", "SecurityException: ${e.message}")
            _weatherState.value = WeatherState.Error("Location permission required")
            onLocationResult("Hanoi")
        }
    }

    private fun hasLocationPermission(): Boolean {
        return permissionManager.getLocationWifiPermissions().all {
            permissionManager.isPermissionGranted(it)
        }
    }
}