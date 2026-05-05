package com.example.claro.ui.screens.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.claro.R
import com.example.claro.ui.theme.*

@Composable
fun SettingsScreen(
    onBackClick: () -> Unit
) {
    var isCelsius by remember { mutableStateOf(true) }
    var rainAlert by remember { mutableStateOf(true) }
    var tempAlert by remember { mutableStateOf(true) }

    val backgroundBrush = Brush.verticalGradient(
        colors = listOf(HomeGradientTop, HomeGradientBottom)
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundBrush)
    ) {
        Scaffold(
            containerColor = Color.Transparent,
            modifier = Modifier.fillMaxSize()
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .verticalScroll(rememberScrollState())
            ) {

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Voltar", tint = TextPrimaryLight)
                    }
                    Text(
                        text = "Configurações",
                        fontFamily = Inter,
                        fontWeight = FontWeight.Medium,
                        fontSize = 18.sp,
                        color = TextPrimaryLight,
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 48.dp),
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                SectionTitle("UNIDADES")
                UnitToggle(
                    isCelsius = isCelsius,
                    onToggle = { isCelsius = it }
                )

                Spacer(modifier = Modifier.height(32.dp))

                SectionTitle("NOTIFICAÇÕES")
                NotificationCard(
                    title = "Alerta de chuva",
                    subtitle = "Receber alertas de chuva",
                    iconRes = android.R.drawable.ic_popup_reminder,
                    isChecked = rainAlert,
                    onCheckedChange = { rainAlert = it }
                )
                Spacer(modifier = Modifier.height(12.dp))
                NotificationCard(
                    title = "Temperatura extrema",
                    subtitle = "Alertas de calor ou frio",
                    iconRes = android.R.drawable.ic_menu_sort_by_size,
                    isChecked = tempAlert,
                    onCheckedChange = { tempAlert = it }
                )

                Spacer(modifier = Modifier.height(32.dp))


                SectionTitle("GERAL")
                GeneralSettingsCard()

                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}


@Composable
private fun SectionTitle(title: String) {
    Text(
        text = title,
        fontFamily = Inter,
        fontSize = 10.sp,
        letterSpacing = 2.sp,
        color = TextSecondaryLight,
        modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp)
    )
}

@Composable
private fun UnitToggle(isCelsius: Boolean, onToggle: (Boolean) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .height(56.dp)
            .clip(RoundedCornerShape(28.dp))
            .background(Color.White)
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .clip(RoundedCornerShape(28.dp))
                .background(if (isCelsius) AccentYellow else Color.Transparent)
                .clickable { onToggle(true) },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "°C",
                fontFamily = Inter,
                fontWeight = if (isCelsius) FontWeight.Medium else FontWeight.Normal,
                fontSize = 16.sp,
                color = TextPrimaryLight
            )
        }

        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .clip(RoundedCornerShape(28.dp))
                .background(if (!isCelsius) AccentYellow else Color.Transparent)
                .clickable { onToggle(false) },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "°F",
                fontFamily = Inter,
                fontWeight = if (!isCelsius) FontWeight.Medium else FontWeight.Normal,
                fontSize = 16.sp,
                color = TextPrimaryLight
            )
        }
    }
}

@Composable
private fun NotificationCard(
    title: String,
    subtitle: String,
    iconRes: Int,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(Color.White)
            .padding(horizontal = 16.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(Color(0xFFF5F0E6)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = iconRes),
                contentDescription = null,
                tint = TextSecondaryLight,
                modifier = Modifier.size(20.dp)
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(text = title, fontFamily = Inter, fontWeight = FontWeight.Medium, fontSize = 14.sp, color = TextPrimaryLight)
            Text(text = subtitle, fontFamily = Inter, fontSize = 12.sp, color = TextSecondaryLight)
        }

        Switch(
            checked = isChecked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = AccentYellow,
                uncheckedThumbColor = Color.White,
                uncheckedTrackColor = Color.LightGray,
                uncheckedBorderColor = Color.Transparent
            )
        )
    }
}

@Composable
private fun GeneralSettingsCard() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(Color.White)
    ) {
        GeneralItemRow(title = "Localização", value = "São Paulo, SP")
        HorizontalDivider(color = TextSecondaryLight.copy(alpha = 0.1f), modifier = Modifier.padding(horizontal = 16.dp))
        GeneralItemRow(title = "Atualizar frequência", value = "A cada 30 min")
        HorizontalDivider(color = TextSecondaryLight.copy(alpha = 0.1f), modifier = Modifier.padding(horizontal = 16.dp))
        GeneralItemRow(title = "Idioma", value = "Português")
    }
}

@Composable
private fun GeneralItemRow(title: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { }
            .padding(horizontal = 16.dp, vertical = 20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = title, fontFamily = Inter, fontWeight = FontWeight.Medium, fontSize = 14.sp, color = TextPrimaryLight)
        Spacer(modifier = Modifier.weight(1f))
        Text(text = value, fontFamily = Inter, fontSize = 12.sp, color = TextSecondaryLight)
        Spacer(modifier = Modifier.width(8.dp))
        Icon(Icons.Default.KeyboardArrowRight, contentDescription = null, tint = TextSecondaryLight, modifier = Modifier.size(16.dp))
    }
}