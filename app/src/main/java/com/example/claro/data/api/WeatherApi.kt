package com.example.claro.data.api

import com.example.claro.data.model.WeatherResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {

    @GET("weather")
    suspend fun getCurrentWeather(
        @Query("q") cityName: String,
        @Query("appid") apiKey: String,
        @Query("units") units: String = "metric",
        @Query("lang") language: String = "pt_br"
    ): WeatherResponse
}