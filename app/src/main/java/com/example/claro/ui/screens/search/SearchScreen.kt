package com.example.claro.ui.screens.search

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.claro.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    onBackClick: () -> Unit,
    onCitySelected: (String) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current

    val backgroundBrush = Brush.verticalGradient(
        colors = listOf(HomeGradientTop, HomeGradientBottom)
    )

    val allAvailableCities = remember {
        listOf(
            CitySuggestion("São Paulo, SP", "Brasil", "28", android.R.drawable.ic_menu_mylocation),
            CitySuggestion("Santiago", "Chile", "18", android.R.drawable.ic_menu_mylocation),
            CitySuggestion("San Diego, CA", "EUA", "22", android.R.drawable.ic_menu_mylocation),
            CitySuggestion("San Francisco, CA", "EUA", "19", android.R.drawable.ic_menu_mylocation),
            CitySuggestion("Santa Cruz", "Bolívia", "17", android.R.drawable.ic_menu_mylocation),
            CitySuggestion("Rio de Janeiro, RJ", "Brasil", "30", android.R.drawable.ic_menu_mylocation),
            CitySuggestion("Tóquio", "Japão", "15", android.R.drawable.ic_menu_mylocation),
            CitySuggestion("Nova York, NY", "EUA", "10", android.R.drawable.ic_menu_mylocation),
            CitySuggestion("Londres", "Reino Unido", "12", android.R.drawable.ic_menu_mylocation),
            CitySuggestion("Paris", "França", "14", android.R.drawable.ic_menu_mylocation),
            CitySuggestion("Buenos Aires", "Argentina", "20", android.R.drawable.ic_menu_mylocation)
        )
    }

    val displayedCities = remember(searchQuery) {
        if (searchQuery.isBlank()) {
            allAvailableCities.take(5)
        } else {
            allAvailableCities.filter { city ->
                city.name.contains(searchQuery, ignoreCase = true) ||
                        city.country.contains(searchQuery, ignoreCase = true)
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize().background(backgroundBrush)) {

        Scaffold(
            containerColor = Color.Transparent,
            modifier = Modifier.fillMaxSize()
        ) { paddingValues ->

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
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
                        text = "Buscar cidade",
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

                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 8.dp),
                    placeholder = {
                        Text("Digite o nome da cidade", fontFamily = Inter, color = TextSecondaryLight)
                    },
                    leadingIcon = {
                        Icon(Icons.Default.Search, contentDescription = "Pesquisar", tint = TextSecondaryLight)
                    },
                    shape = RoundedCornerShape(percent = 50),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color.White.copy(alpha = 0.5f),
                        unfocusedContainerColor = Color.White.copy(alpha = 0.5f),
                        focusedBorderColor = AccentYellow,
                        unfocusedBorderColor = Color.Transparent,
                        cursorColor = AccentYellow
                    ),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                    keyboardActions = KeyboardActions(
                        onSearch = {
                            if (searchQuery.isNotBlank()) {
                                keyboardController?.hide()
                                onCitySelected(searchQuery)
                            }
                        }
                    )
                )

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = if (searchQuery.isBlank()) "SUGERÊNCIAS" else "RESULTADOS",
                    fontFamily = Inter,
                    fontSize = 10.sp,
                    letterSpacing = 2.sp,
                    color = TextSecondaryLight,
                    modifier = Modifier.padding(horizontal = 24.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))

                LazyColumn(
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(horizontal = 24.dp, vertical = 8.dp)
                ) {
                    items(displayedCities) { city ->
                        SuggestionItem(
                            suggestion = city,
                            onClick = {
                                keyboardController?.hide()
                                onCitySelected(city.name.split(",")[0])
                            }
                        )
                        HorizontalDivider(color = TextSecondaryLight.copy(alpha = 0.1f))
                    }

                    if (displayedCities.isEmpty()) {
                        item {
                            Text(
                                text = "Aperte a lupa no teclado para buscar '$searchQuery' no mundo todo.",
                                fontFamily = Inter,
                                color = TextSecondaryLight,
                                modifier = Modifier
                                    .padding(vertical = 24.dp)
                                    .fillMaxWidth(),
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center
                            )
                        }
                    }
                }
            }
        }
    }
}


data class CitySuggestion(val name: String, val country: String, val temp: String, val iconRes: Int)

@Composable
private fun SuggestionItem(suggestion: CitySuggestion, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = suggestion.iconRes),
            contentDescription = null,
            tint = AccentYellow,
            modifier = Modifier.size(24.dp)
        )

        Spacer(modifier = Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = suggestion.name,
                fontFamily = PlayfairDisplay,
                fontSize = 18.sp,
                color = TextPrimaryLight
            )
            Text(
                text = suggestion.country,
                fontFamily = Inter,
                fontSize = 12.sp,
                color = TextSecondaryLight
            )
        }

        Row(verticalAlignment = Alignment.Top) {
            Text(
                text = suggestion.temp,
                fontFamily = PlayfairDisplay,
                fontSize = 20.sp,
                color = TextPrimaryLight
            )
            Text(
                text = "°",
                fontFamily = PlayfairDisplay,
                fontSize = 12.sp,
                color = TextPrimaryLight,
                modifier = Modifier.padding(top = 2.dp)
            )
        }
    }
}