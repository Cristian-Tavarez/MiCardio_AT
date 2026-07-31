package com.example.micardioat.domain.model

data class PacienteCardiologia(
    val pacienteId: Int? = null,
    val nombre: String = "",
    val edad: Int = 0,
    val diagnostico: String = "",
    val presionArterial: String = ""
)