package com.example.micardioat.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.example.micardioat.presentation.login.LoginScreen
import com.example.micardioat.presentation.paciente_list.PacienteEditScreen
import com.example.micardioat.presentation.paciente_list.PacienteListScreen
import com.example.micardioat.presentation.paciente_list.PatientsScreen
import com.example.micardioat.presentation.register.RegisterScreen
import com.example.micardioat.presentation.settings.SettingsScreen
import com.example.micardioat.presentation.splash.SplashScreen
import com.example.micardioat.utils.AppThemeMode
import com.google.firebase.auth.FirebaseAuth
import com.example.micardioat.presentation.paciente_list.PacienteAddViewModel
@Composable
fun AppNavigation(
    currentTheme: AppThemeMode = AppThemeMode.SYSTEM,
    onThemeChange: (AppThemeMode) -> Unit = {}
) {
    val backStack = remember {
        mutableStateListOf<Screen>(Screen.Splash)
    }

    MainAppScreen(
        backStack = backStack
    ) { modifier ->

        NavDisplay(
            backStack = backStack,
            modifier = modifier,
            onBack = {
                backStack.removeLastOrNull()
            },
            entryDecorators = listOf(
                rememberSaveableStateHolderNavEntryDecorator(),
                rememberViewModelStoreNavEntryDecorator()
            ),
            entryProvider = entryProvider {

                entry<Screen.Splash> {
                    SplashScreen(
                        onSplashFinished = {
                            val currentUser =
                                FirebaseAuth.getInstance().currentUser

                            backStack.clear()
                            backStack.add(
                                if (currentUser != null) {
                                    Screen.PacienteList
                                } else {
                                    Screen.Login
                                }
                            )
                        }
                    )
                }

                entry<Screen.Login> {
                    LoginScreen(
                        onLoginSuccess = {
                            backStack.clear()
                            backStack.add(Screen.PacienteList)
                        },
                        onNavigateToRegister = {
                            backStack.add(Screen.Register)
                        }
                    )
                }

                entry<Screen.Register> {
                    RegisterScreen(
                        onRegisterSuccess = {
                            backStack.clear()
                            backStack.add(Screen.PacienteList)
                        },
                        onNavigateBack = {
                            backStack.removeLastOrNull()
                        }
                    )
                }

                entry<Screen.PacienteList> {
                    PacienteListScreen(
                        onNavigateToDetail = { id ->
                            backStack.add(
                                Screen.PacienteEdit(id)
                            )
                        }
                    )
                }

                entry<Screen.PacienteEdit> { key ->

                    val viewModel =
                        hiltViewModel<
                                PacienteAddViewModel,
                                PacienteAddViewModel.Factory
                                >(
                            creationCallback = { factory ->
                                factory.create(key)
                            }
                        )

                    PacienteEditScreen(
                        viewModel = viewModel,
                        onNavigateBack = {
                            backStack.removeLastOrNull()
                        }
                    )
                }

                entry<Screen.Patients> {
                    PatientsScreen(
                        onNavigateToDetail = { id ->
                            backStack.add(
                                Screen.PacienteEdit(id)
                            )
                        },
                        onAddPatient = {
                            backStack.add(
                                Screen.PacienteEdit()
                            )
                        }
                    )
                }

                entry<Screen.Settings> {
                    SettingsScreen(
                        currentTheme = currentTheme,
                        onThemeSelected = onThemeChange,
                        onLogout = {
                            FirebaseAuth.getInstance().signOut()

                            backStack.clear()
                            backStack.add(Screen.Login)
                        }
                    )
                }
            }
        )
    }
}