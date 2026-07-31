package com.example.micardioat.presentation.navigation

import kotlinx.serialization.Serializable

sealed class Screen {
    @Serializable
    data object PacienteList : Screen()

    @Serializable
    data class PacienteEdit(val pacienteId: Int? = null) : Screen()
}