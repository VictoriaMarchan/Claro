package com.example.claro.data.model


sealed interface WeatherUiState {

    object Loading : WeatherUiState

    data class Success(val weather: WeatherResponse) : WeatherUiState

    data class Error(val message: String) : WeatherUiState
}