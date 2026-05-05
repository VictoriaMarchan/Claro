package com.example.claro.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.claro.ui.screens.details.DetailsScreen
import com.example.claro.ui.screens.home.HomeScreen
import com.example.claro.ui.screens.search.SearchScreen
import com.example.claro.ui.screens.settings.SettingsScreen
import com.example.claro.ui.screens.splash.SplashScreen
import com.example.claro.viewmodel.WeatherViewModel

@Composable
fun ClaroNavGraph(navController: NavHostController, viewModel: WeatherViewModel) {

    NavHost(navController = navController, startDestination = "splash") {

        composable("splash") {
            SplashScreen(
                viewModel = viewModel,
                onNavigateToHome = {
                    navController.navigate("home") {
                        popUpTo("splash") { inclusive = true }
                    }
                }
            )
        }

        composable("home") {
            HomeScreen(
                viewModel = viewModel,
                onNavigateToSearch = { navController.navigate("search") },
                onNavigateToSettings = { navController.navigate("settings") },
                onNavigateToDetails = { navController.navigate("details") }
            )
        }

        composable("search") {
            SearchScreen(
                onBackClick = { navController.popBackStack() },
                onCitySelected = { cityName ->
                    viewModel.getWeather(cityName)
                    navController.popBackStack()
                }
            )
        }

        composable("details") {
            DetailsScreen(
                viewModel = viewModel,
                onBackClick = { navController.popBackStack() }
            )
        }

        composable("settings") {
            SettingsScreen(
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}