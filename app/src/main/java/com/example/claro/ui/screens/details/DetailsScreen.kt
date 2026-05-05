package com.example.claro.ui.screens.details

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.claro.R
import com.example.claro.data.model.WeatherUiState
import com.example.claro.ui.theme.*
import com.example.claro.viewmodel.WeatherViewModel

@Composable
fun DetailsScreen(
    viewModel: WeatherViewModel,
    onBackClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    val backgroundBrush = Brush.verticalGradient(
        colors = listOf(DarkNavy, Color(0xFF0F131E))
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundBrush)
            .systemBarsPadding()
    ) {
        when (val state = uiState) {
            is WeatherUiState.Loading -> CircularProgressIndicator(modifier = Modifier.align(Alignment.Center), color = AccentYellow)
            is WeatherUiState.Error -> Text(state.message, color = MaterialTheme.colorScheme.error, modifier = Modifier.align(Alignment.Center).padding(16.dp))
            is WeatherUiState.Success -> {
                val weather = state.weather

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 24.dp)
                ) {
                    DetailsTopBar(
                        cityName = weather.cityName.uppercase(),
                        onBackClick = onBackClick
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    HeroSection(
                        temperature = weather.mainMetrics.temperature.toInt().toString(),
                        condition = weather.weatherConditions.firstOrNull()?.description?.replaceFirstChar { it.uppercase() } ?: "Ensolarado",
                        date = "Segunda, 4 de Maio",
                        iconCode = weather.weatherConditions.firstOrNull()?.iconId
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    WeatherDetailsGrid(
                        feelsLike = "${weather.mainMetrics.feelsLike.toInt()}°C",
                        wind = "${weather.windInfo.speed.toInt()} km/h NO",
                        humidity = "${weather.mainMetrics.humidity}%",
                        pressure = "${weather.mainMetrics.pressure} hPa",
                        visibility = "12 km"
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    SunPathChart(sunrise = "06:22", sunset = "17:42")

                    Spacer(modifier = Modifier.height(32.dp))
                }
            }
        }
    }
}

@Composable
private fun DetailsTopBar(cityName: String, onBackClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onBackClick, modifier = Modifier.size(24.dp)) {
            Icon(Icons.Default.ArrowBack, contentDescription = "Voltar", tint = Color.White)
        }

        Text(
            text = cityName.ifBlank { "São Paulo, SP" },
            fontFamily = Inter,
            fontWeight = FontWeight.Medium,
            fontSize = 14.sp,
            letterSpacing = 1.sp,
            color = Color.White
        )

        IconButton(onClick = { }, modifier = Modifier.size(24.dp)) {
            Icon(Icons.Default.MoreVert, contentDescription = "Opções", tint = Color.White)
        }
    }
}

@Composable
private fun HeroSection(temperature: String, condition: String, date: String, iconCode: String?) {
    val geometricIconRes = remember(iconCode) {
        when (iconCode) {
            "01d" -> R.drawable.img_sun_large
            "01n" -> R.drawable.img_moon_large
            "02d", "02n" -> R.drawable.img_cloud_large
            else -> {
                when {
                    iconCode?.contains("09") == true || iconCode?.contains("10") == true -> R.drawable.img_rain_large
                    iconCode?.contains("13") == true -> R.drawable.img_snow_large
                    else -> R.drawable.img_cloud_large
                }
            }
        }
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Row(verticalAlignment = Alignment.Top) {
                Text(
                    text = temperature,
                    fontFamily = PlayfairDisplay,
                    fontSize = 80.sp,
                    color = Color.White,
                    modifier = Modifier.offset(y = (-8).dp)
                )
                Text(
                    text = "°C",
                    fontFamily = PlayfairDisplay,
                    fontSize = 32.sp,
                    color = Color.White,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
            Text(text = condition, fontFamily = Inter, fontSize = 20.sp, color = Color.White)
            Text(text = date, fontFamily = Inter, fontSize = 14.sp, color = TextSecondaryDark)
        }

        Image(
            painter = painterResource(id = geometricIconRes),
            contentDescription = condition,
            modifier = Modifier.size(120.dp)
        )
    }
}

@Composable
private fun WeatherDetailsGrid(feelsLike: String, wind: String, humidity: String, pressure: String, visibility: String) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            DetailCard(modifier = Modifier.weight(1f), title = "SENSAÇÃO TÉRMICA", value = feelsLike, iconRes = R.drawable.ic_thermometer)
            DetailCard(modifier = Modifier.weight(1f), title = "VENTO", value = wind, iconRes = R.drawable.ic_wind)
        }

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            DetailCard(modifier = Modifier.weight(1f), title = "UMIDADE", value = humidity, iconRes = R.drawable.ic_humidity)
            DetailCard(modifier = Modifier.weight(1f), title = "PRESSÃO", value = pressure, iconRes = R.drawable.ic_pressure)
            DetailCard(modifier = Modifier.weight(1f), title = "VISIBILIDADE", value = visibility, iconRes = R.drawable.ic_visibility)
        }
    }
}

@Composable
private fun DetailCard(modifier: Modifier = Modifier, title: String, value: String, iconRes: Int) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White.copy(alpha = 0.05f))
            .padding(16.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
            Icon(
                painter = painterResource(id = iconRes),
                contentDescription = title,
                tint = AccentYellow,
                modifier = Modifier.size(28.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = title, fontFamily = Inter, fontSize = 9.sp, color = TextSecondaryDark, letterSpacing = 1.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = value, fontFamily = Inter, fontSize = 16.sp, color = Color.White, fontWeight = FontWeight.Medium)
        }
    }
}

@Composable
private fun SunPathChart(sunrise: String, sunset: String) {
    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        Box(modifier = Modifier.fillMaxWidth().height(80.dp)) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val width = size.width
                val height = size.height

                val path = Path().apply {
                    moveTo(0f, height)
                    quadraticBezierTo(width / 2, -height / 2, width, height)
                }

                drawPath(
                    path = path,
                    color = AccentYellow.copy(alpha = 0.6f),
                    style = Stroke(
                        width = 4f,
                        pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
                    )
                )

                drawCircle(color = AccentYellow, radius = 6f, center = Offset(0f, height))
                drawCircle(color = AccentYellow, radius = 6f, center = Offset(width, height))
                drawCircle(color = AccentYellow, radius = 12f, center = Offset(width * 0.8f, height * 0.45f))
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(text = sunrise, fontFamily = Inter, fontSize = 12.sp, color = Color.White)
            Text(text = "Nascer e Pôr do Sol", fontFamily = Inter, fontSize = 12.sp, color = TextSecondaryDark)
            Text(text = sunset, fontFamily = Inter, fontSize = 12.sp, color = Color.White)
        }
    }
}