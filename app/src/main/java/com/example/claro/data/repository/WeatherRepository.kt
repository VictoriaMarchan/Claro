package com.example.claro.data.repository

import com.example.claro.data.api.RetrofitClient
import com.example.claro.data.api.WeatherApi
import com.example.claro.data.model.WeatherResponse

class WeatherRepository {

    private val api = RetrofitClient.instance.create(WeatherApi::class.java)
    suspend fun fetchWeather(cityName: String, apiKey: String): WeatherResponse {
        return api.getCurrentWeather(cityName = cityName, apiKey = apiKey)
    }
}