package com.example.micardioat.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.micardioat.presentation.login.LoginScreen
import com.example.micardioat.presentation.paciente_list.PacienteEditScreen
import com.example.micardioat.presentation.paciente_list.PacienteListScreen
import com.example.micardioat.presentation.register.RegisterScreen
import com.google.firebase.auth.FirebaseAuth

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    val currentUser = FirebaseAuth.getInstance().currentUser
    val startDestination: Screen = if (currentUser != null) Screen.PacienteList else Screen.Login

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable<Screen.Login> {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Screen.PacienteList) {
                        popUpTo<Screen.Login> { inclusive = true }
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
                        popUpTo<Screen.Login> { inclusive = true }
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
                },
                onLogout = {
                    FirebaseAuth.getInstance().signOut()
                    navController.navigate(Screen.Login) {
                        popUpTo(0) { inclusive = true }
                    }
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