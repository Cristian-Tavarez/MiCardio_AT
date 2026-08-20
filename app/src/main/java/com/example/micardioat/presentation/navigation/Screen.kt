package com.example.micardioat.presentation.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

sealed class Screen : NavKey {

    @Serializable
    data object Splash : Screen()

    @Serializable
    data object Login : Screen()

    @Serializable
    data object Register : Screen()

    @Serializable
    data object PacienteList : Screen()

    @Serializable
    data class PacienteEdit(
        val pacienteId: Int? = null
    ) : Screen()

    @Serializable
    data object Patients : Screen()

    @Serializable
    data object Settings : Screen()
}