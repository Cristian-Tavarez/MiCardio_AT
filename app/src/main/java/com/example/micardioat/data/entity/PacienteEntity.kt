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
    val presionArterial: String,
    val sexo: String = "",
    val motivoConsulta: String = "",
    val fc: String = "",
    val fr: String = "",
    val antecedentesQuirurgicos: String = "",
    val antecedentesPatologicos: String = "",
    val tratamiento: String = "",
    val alergias: String = "",
    val hb: String = "",
    val hct: String = "",
    val glicemia: String = "",
    val colTotal: String = "",
    val fevi: String = "",
    val plan: String = ""
)

fun PacienteEntity.toDomain(): PacienteCardiologia {
    return PacienteCardiologia(
        pacienteId = pacienteId,
        nombre = nombre,
        edad = edad,
        diagnostico = diagnostico,
        presionArterial = presionArterial,
        sexo = sexo,
        motivoConsulta = motivoConsulta,
        fc = fc,
        fr = fr,
        antecedentesQuirurgicos = antecedentesQuirurgicos,
        antecedentesPatologicos = antecedentesPatologicos,
        tratamiento = tratamiento,
        alergias = alergias,
        hb = hb,
        hct = hct,
        glicemia = glicemia,
        colTotal = colTotal,
        fevi = fevi,
        plan = plan
    )
}

fun PacienteCardiologia.toEntity(): PacienteEntity {
    return PacienteEntity(
        pacienteId = pacienteId,
        nombre = nombre,
        edad = edad,
        diagnostico = diagnostico,
        presionArterial = presionArterial,
        sexo = sexo,
        motivoConsulta = motivoConsulta,
        fc = fc,
        fr = fr,
        antecedentesQuirurgicos = antecedentesQuirurgicos,
        antecedentesPatologicos = antecedentesPatologicos,
        tratamiento = tratamiento,
        alergias = alergias,
        hb = hb,
        hct = hct,
        glicemia = glicemia,
        colTotal = colTotal,
        fevi = fevi,
        plan = plan
    )
}