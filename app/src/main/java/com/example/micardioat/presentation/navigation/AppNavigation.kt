package com.example.micardioat.presentation.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.micardioat.presentation.login.LoginScreen
import com.example.micardioat.presentation.paciente_list.PacienteEditScreen
import com.example.micardioat.presentation.paciente_list.PacienteListScreen
import com.example.micardioat.presentation.paciente_list.PatientsScreen
import com.example.micardioat.presentation.register.RegisterScreen
import com.example.micardioat.presentation.settings.SettingsScreen
import com.example.micardioat.presentation.splash.SplashScreen
import com.google.firebase.auth.FirebaseAuth

@Composable
fun AppNavigation(
    isDarkMode: Boolean = false,
    onDarkModeToggle: (Boolean) -> Unit = {}
) {
    val navController = rememberNavController()

    MainAppScreen(navController = navController) { modifier ->
        NavHost(
            navController = navController,
            startDestination = Screen.Splash,
            modifier = modifier
        ) {
            composable<Screen.Splash> {
                SplashScreen(
                    onSplashFinished = {
                        val currentUser = FirebaseAuth.getInstance().currentUser
                        val destination = if (currentUser != null) Screen.PacienteList else Screen.Login

                        navController.navigate(destination) {
                            popUpTo<Screen.Splash> { inclusive = true }
                        }
                    }
                )
            }

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

            composable<Screen.Patients> {
                PatientsScreen(
                    onNavigateToDetail = { id ->
                        navController.navigate(Screen.PacienteEdit(pacienteId = id))
                    },
                    onAddPatient = {
                        navController.navigate(Screen.PacienteEdit())
                    }
                )
            }

            composable<Screen.Settings> {
                SettingsScreen(
                    isDarkMode = isDarkMode,
                    onDarkModeToggle = onDarkModeToggle,
                    onLogout = {
                        FirebaseAuth.getInstance().signOut()
                        navController.navigate(Screen.Login) {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                )
            }
        }
    }
}