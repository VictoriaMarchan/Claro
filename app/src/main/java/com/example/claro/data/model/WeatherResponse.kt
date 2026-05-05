package com.example.claro.data.model

import com.google.gson.annotations.SerializedName

data class WeatherResponse(
    @SerializedName("name")
    val cityName: String,

    @SerializedName("main")
    val mainMetrics: MainMetrics,

    @SerializedName("weather")
    val weatherConditions: List<WeatherCondition>,

    @SerializedName("wind")
    val windInfo: WindInfo
)

data class MainMetrics(
    @SerializedName("temp")
    val temperature: Double,

    @SerializedName("feels_like")
    val feelsLike: Double,

    @SerializedName("humidity")
    val humidity: Int,

    @SerializedName("pressure")
    val pressure: Int
)

data class WeatherCondition(
    @SerializedName("description")
    val description: String,

    @SerializedName("icon")
    val iconId: String
)

data class WindInfo(
    @SerializedName("speed")
    val speed: Double
)