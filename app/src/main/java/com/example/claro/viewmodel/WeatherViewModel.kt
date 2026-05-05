package com.example.claro.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.claro.data.model.WeatherUiState
import com.example.claro.data.repository.WeatherRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class WeatherViewModel : ViewModel() {

    private val repository = WeatherRepository()

    private val _uiState = MutableStateFlow<WeatherUiState>(WeatherUiState.Loading)
    val uiState: StateFlow<WeatherUiState> = _uiState.asStateFlow()

    private val API_KEY = "f663de402141bfe3a7d8bc18306b668a"

    init {
        getWeather("São Paulo")
    }

    fun getWeather(city: String) {
        viewModelScope.launch {
            _uiState.value = WeatherUiState.Loading

            try {
                val response = repository.fetchWeather(cityName = city, apiKey = API_KEY)

                _uiState.value = WeatherUiState.Success(response)

            } catch (e: Exception) {
                _uiState.value = WeatherUiState.Error("Não foi possível buscar o clima: ${e.localizedMessage}")
            }
        }
    }
}