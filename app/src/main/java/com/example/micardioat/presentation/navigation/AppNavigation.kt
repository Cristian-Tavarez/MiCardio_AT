package com.example.micardioat.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.micardioat.presentation.paciente_list.PacienteEditScreen
import com.example.micardioat.presentation.paciente_list.PacienteListScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.PacienteList
    ) {
        composable<Screen.PacienteList> {
            PacienteListScreen(
                onNavigateToDetail = { id ->
                    navController.navigate(Screen.PacienteEdit(pacienteId = id))
                }
            )
        }

        composable<Screen.PacienteEdit> {
            PacienteEditScreen(
                onNavigateBack = {
                    navController.navigateUp()
                }
            )
        }
    }
}