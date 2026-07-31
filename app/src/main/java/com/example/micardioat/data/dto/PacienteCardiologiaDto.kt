package com.example.micardioat.data.dto

import com.example.micardioat.domain.model.PacienteCardiologia
import kotlinx.serialization.Serializable

@Serializable
data class PacienteCardiologiaDto(
    val pacienteId: Int? = null,
    val nombre: String = "",
    val edad: Int = 0,
    val diagnostico: String = "",
    val presionArterial: String = ""
)

fun PacienteCardiologiaDto.toDomain(): PacienteCardiologia {
    return PacienteCardiologia(
        pacienteId = pacienteId,
        nombre = nombre,
        edad = edad,
        diagnostico = diagnostico,
        presionArterial = presionArterial
    )
}

fun PacienteCardiologia.toDto(): PacienteCardiologiaDto {
    return PacienteCardiologiaDto(
        pacienteId = pacienteId,
        nombre = nombre,
        edad = edad,
        diagnostico = diagnostico,
        presionArterial = presionArterial
    )
}