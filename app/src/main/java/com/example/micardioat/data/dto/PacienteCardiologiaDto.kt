package com.example.micardioat.data.dto

import com.example.micardioat.domain.model.Paciente
import com.example.micardioat.domain.model.PacienteDetalle
import com.example.micardioat.domain.model.Visita
import kotlinx.serialization.Serializable

@Serializable
data class PacienteCardiologiaDto(
    val pacienteId: Int? = null,
    val nombre: String = "",
    val edad: Int = 0,
    val diagnostico: String = "",
    val presionArterial: String = "",
    val fechaCita: Long? = null
)

fun PacienteCardiologiaDto.toDomain(): PacienteDetalle {
    return PacienteDetalle(
        paciente = Paciente(
            pacienteId = pacienteId,
            nombre = nombre,
            edad = edad
        ),
        visitas = if (fechaCita != null || diagnostico.isNotBlank()) {
            listOf(
                Visita(
                    pacienteId = pacienteId ?: 0,
                    diagnostico = diagnostico,
                    presionArterial = presionArterial,
                    fechaCita = fechaCita
                )
            )
        } else {
            emptyList()
        }
    )
}

fun PacienteDetalle.toDto(): PacienteCardiologiaDto {
    val ultimaVisita = visitas.maxByOrNull { it.fechaCita ?: 0L }

    return PacienteCardiologiaDto(
        pacienteId = paciente.pacienteId,
        nombre = paciente.nombre,
        edad = paciente.edad,
        diagnostico = ultimaVisita?.diagnostico ?: "",
        presionArterial = ultimaVisita?.presionArterial ?: "",
        fechaCita = ultimaVisita?.fechaCita
    )
}