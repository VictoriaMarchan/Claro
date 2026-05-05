package com.example.claro.ui.screens.splash

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import com.example.claro.R
import com.example.claro.data.model.WeatherUiState
import com.example.claro.ui.theme.AccentYellow
import com.example.claro.ui.theme.Inter
import com.example.claro.ui.theme.PlayfairDisplay
import com.example.claro.viewmodel.WeatherViewModel

@Composable
fun SplashScreen(
    viewModel: WeatherViewModel,
    onNavigateToHome: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val backgroundImageRes = remember(uiState) {
        when (uiState) {
            is WeatherUiState.Success -> {
                val iconCode = (uiState as WeatherUiState.Success).weather.weatherConditions.firstOrNull()?.iconId

                when {
                    iconCode?.contains("01") == true -> R.drawable.bg_splash_sunset
                    iconCode?.contains("09") == true || iconCode?.contains("10") == true -> R.drawable.bg_splash_rain
                    iconCode?.contains("13") == true -> R.drawable.bg_splash_snow
                    else -> R.drawable.bg_splash_clouds
                }
            }
            else -> R.drawable.bg_splash_sunset
        }
    }

    var progress by remember { mutableFloatStateOf(0f) }
    var isTextVisible by remember { mutableStateOf(false) }

    val textAlpha by animateFloatAsState(
        targetValue = if (isTextVisible) 1f else 0f,
        animationSpec = tween(durationMillis = 1500),
        label = "FadeInText"
    )

    LaunchedEffect(key1 = true) {
        delay(300)
        isTextVisible = true

        while (progress < 1f) {
            delay(30)
            progress += 0.015f
        }

        delay(400)
        onNavigateToHome()
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = backgroundImageRes),
            contentDescription = "Fundo do Clima",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color.Transparent, Color(0xCC0B101E)),
                        startY = 1000f
                    )
                )
        )

        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(start = 32.dp, end = 32.dp, bottom = 80.dp)
        ) {
            Text(
                text = buildAnnotatedString {
                    append("claro")
                    withStyle(style = SpanStyle(color = AccentYellow)) { append(".") }
                },
                fontFamily = PlayfairDisplay,
                fontSize = 64.sp,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Clareza para o seu dia,\nem qualquer clima.",
                fontFamily = Inter,
                fontSize = 16.sp,
                color = Color.White.copy(alpha = 0.8f),
                lineHeight = 24.sp
            )

            Spacer(modifier = Modifier.height(48.dp))

            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .fillMaxWidth(0.4f)
                    .height(3.dp),
                color = AccentYellow,
                trackColor = Color.White.copy(alpha = 0.2f),
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Carregando...",
                fontFamily = Inter,
                fontSize = 12.sp,
                color = Color.White.copy(alpha = 0.6f),
                modifier = Modifier.alpha(textAlpha)
            )
        }
    }
}