package com.example.claro

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.claro.ui.navigation.ClaroNavGraph
import com.example.claro.viewmodel.WeatherViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            val weatherViewModel: WeatherViewModel = viewModel()

            ClaroNavGraph(
                navController = navController,
                viewModel = weatherViewModel
            )
        }
    }
}