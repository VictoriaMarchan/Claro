package com.example.claro.ui.screens.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.claro.R
import com.example.claro.data.model.WeatherUiState
import com.example.claro.ui.theme.*
import com.example.claro.viewmodel.WeatherViewModel

@Composable
fun HomeScreen(
    viewModel: WeatherViewModel,
    onNavigateToSearch: () -> Unit,
    onNavigateToSettings: () -> Unit,
    onNavigateToDetails: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    val backgroundBrush = Brush.verticalGradient(
        colors = listOf(HomeGradientTop, HomeGradientBottom)
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

                Column(modifier = Modifier.fillMaxSize()) {

                    HomeTopBar(
                        cityName = weather.cityName,
                        onMenuClick = onNavigateToSettings,
                        onLocationClick = onNavigateToSearch,
                        onCityClick = onNavigateToDetails
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    CurrentWeatherSection(
                        temperature = weather.mainMetrics.temperature.toInt().toString(),
                        condition = weather.weatherConditions.firstOrNull()?.description?.replaceFirstChar { it.uppercase() } ?: "Desconhecido",
                        feelsLike = weather.mainMetrics.feelsLike.toInt().toString(),
                        iconCode = weather.weatherConditions.firstOrNull()?.iconId
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    QuickIndicatorsBar(
                        humidity = "${weather.mainMetrics.humidity}%",
                        windSpeed = "${weather.windInfo.speed.toInt()} km/h",
                        uvIndex = "7 Alto"
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    ForecastBottomSheet(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
private fun HomeTopBar(
    cityName: String,
    onMenuClick: () -> Unit,
    onLocationClick: () -> Unit,
    onCityClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onMenuClick) {
            Icon(Icons.Default.Menu, contentDescription = "Menu", tint = TextPrimaryLight)
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .clickable(onClick = onCityClick)
                .padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
            Text(
                text = cityName,
                fontFamily = Inter,
                fontWeight = FontWeight.Medium,
                fontSize = 16.sp,
                color = TextPrimaryLight
            )
            Spacer(modifier = Modifier.width(4.dp))
            Icon(Icons.Default.KeyboardArrowDown, contentDescription = "Detalhes da Cidade", tint = TextPrimaryLight)
        }

        IconButton(onClick = onLocationClick) {
            Icon(Icons.Default.LocationOn, contentDescription = "Buscar Localização", tint = TextPrimaryLight)
        }
    }
}

@Composable
private fun CurrentWeatherSection(temperature: String, condition: String, feelsLike: String, iconCode: String?) {
    val (largeImageRes, smallIconRes) = remember<Pair<Int, Int>>(iconCode) {
        when (iconCode) {
            "01d" -> Pair(R.drawable.img_sun_large,R.drawable.ic_menu_sun)
            "01n" -> Pair(R.drawable.img_moon_large,R.drawable.ic_menu_night)
            "02d", "02n" -> Pair(R.drawable.img_cloud_large, R.drawable.ic_menu_cloud)
            else -> {
                when {
                    iconCode?.contains("09") == true || iconCode?.contains("10") == true -> Pair(R.drawable.img_rain_large, R.drawable.ic_menu_rain)
                    iconCode?.contains("13") == true -> Pair(R.drawable.img_snow_large, R.drawable.ic_menu_snow)
                    else -> Pair(R.drawable.img_cloud_large, R.drawable.ic_menu_cloud)
                }
            }
        }
    }

    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 32.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Row(verticalAlignment = Alignment.Top) {
                Text(text = temperature, fontFamily = PlayfairDisplay, fontSize = 96.sp, color = TextPrimaryLight)
                Text(text = "°", fontFamily = PlayfairDisplay, fontSize = 48.sp, color = TextPrimaryLight, modifier = Modifier.padding(top = 12.dp))
            }
            Text(text = condition, fontFamily = Inter, fontSize = 20.sp, color = TextPrimaryLight)
            Text(text = "Sensação $feelsLike°", fontFamily = Inter, fontSize = 14.sp, color = TextSecondaryLight)
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Image(
                painter = painterResource(id = largeImageRes),
                contentDescription = condition,
                contentScale = ContentScale.Fit,
                modifier = Modifier.size(110.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Box(
                modifier = Modifier.size(44.dp).clip(CircleShape).background(Color.White),
                contentAlignment = Alignment.Center
            ) {
                Icon(painter = painterResource(id = smallIconRes), contentDescription = null, tint = AccentYellow, modifier = Modifier.size(24.dp))
            }
        }
    }
}

@Composable
private fun QuickIndicatorsBar(humidity: String, windSpeed: String, uvIndex: String) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp).clip(RoundedCornerShape(24.dp)).background(IndicatorBackground).padding(16.dp),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IndicatorItem(iconRes = android.R.drawable.ic_menu_info_details, value = humidity)
        IndicatorItem(iconRes = android.R.drawable.ic_menu_send, value = windSpeed)
        Text(text = "UV $uvIndex", fontFamily = Inter, fontSize = 14.sp, color = AccentYellow, fontWeight = FontWeight.SemiBold)
    }
}

@Composable
private fun IndicatorItem(iconRes: Int, value: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(painter = painterResource(id = iconRes), contentDescription = null, tint = TextSecondaryLight, modifier = Modifier.size(16.dp))
        Spacer(modifier = Modifier.width(4.dp))
        Text(text = value, fontFamily = Inter, fontSize = 14.sp, color = TextPrimaryLight)
    }
}

@Composable
fun ForecastBottomSheet(modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
        colors = CardDefaults.cardColors(containerColor = DarkNavy)
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            LazyRow(horizontalArrangement = Arrangement.spacedBy(24.dp), modifier = Modifier.fillMaxWidth()) {
                val hours = listOf("10:00" to "28°", "11:00" to "29°", "12:00" to "31°", "13:00" to "32°", "14:00" to "31°")
                items(hours.size) { index ->
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = hours[index].first, color = TextSecondaryDark, fontSize = 12.sp, fontFamily = Inter)
                        Spacer(modifier = Modifier.height(8.dp))
                        Icon(painter = painterResource(id = android.R.drawable.ic_menu_day), contentDescription = null, tint = AccentYellow)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = hours[index].second, color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Medium, fontFamily = Inter)
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
            Text("PRÓXIMOS DIAS", color = TextSecondaryDark, fontSize = 10.sp, letterSpacing = 2.sp, fontFamily = Inter)
            Spacer(modifier = Modifier.height(16.dp))

            val days = listOf("Amanhã" to ("22°" to "31°"), "Quarta" to ("20°" to "27°"), "Quinta" to ("19°" to "28°"), "Sexta" to ("21°" to "29°"), "Sábado" to ("22°" to "30°"))
            days.forEach { day ->
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = day.first, color = Color.White, modifier = Modifier.weight(1f), fontFamily = Inter)
                    Text(text = day.second.first, color = TextSecondaryDark, modifier = Modifier.padding(end = 12.dp), fontFamily = Inter)
                    Box(modifier = Modifier.weight(1.5f).height(3.dp).clip(RoundedCornerShape(2.dp)).background(Color.White.copy(alpha = 0.1f))) {
                        Box(modifier = Modifier.fillMaxHeight().fillMaxWidth(0.6f).align(Alignment.Center).clip(RoundedCornerShape(2.dp)).background(AccentYellow))
                    }
                    Text(text = day.second.second, color = Color.White, modifier = Modifier.padding(start = 12.dp), fontFamily = Inter)
                }
            }
        }
    }
}