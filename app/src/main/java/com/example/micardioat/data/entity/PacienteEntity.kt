package com.example.micardioat.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.micardioat.domain.model.PacienteCardiologia

@Entity(tableName = "pacientes")
data class PacienteEntity(
    @PrimaryKey(autoGenerate = true)
    val pacienteId: Int? = null,
    val nombre: String,
    val edad: Int,
    val diagnostico: String,
    val presionArterial: String
)

fun PacienteEntity.toDomain(): PacienteCardiologia {
    return PacienteCardiologia(
        pacienteId = pacienteId,
        nombre = nombre,
        edad = edad,
        diagnostico = diagnostico,
        presionArterial = presionArterial
    )
}

fun PacienteCardiologia.toEntity(): PacienteEntity {
    return PacienteEntity(
        pacienteId = pacienteId,
        nombre = nombre,
        edad = edad,
        diagnostico = diagnostico,
        presionArterial = presionArterial
    )
}