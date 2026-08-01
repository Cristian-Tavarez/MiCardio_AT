package com.example.micardioat.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.micardioat.presentation.login.LoginScreen
import com.example.micardioat.presentation.paciente_list.PacienteEditScreen
import com.example.micardioat.presentation.paciente_list.PacienteListScreen
import com.example.micardioat.presentation.register.RegisterScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.Login
    ) {
        composable<Screen.Login> {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Screen.PacienteList) {
                        popUpTo(Screen.Login) { inclusive = true }
                    }
                },
                onNavigateToRegister = {
                    navController.navigate(Screen.Register)
                }
            )
        }

        composable<Screen.Register> {
            RegisterScreen(
                onRegisterSuccess = {
                    navController.navigate(Screen.PacienteList) {
                        popUpTo(Screen.Login) { inclusive = true }
                    }
                },
                onNavigateBack = {
                    navController.navigateUp()
                }
            )
        }

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