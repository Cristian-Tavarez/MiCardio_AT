package com.example.micardioat.data.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.Relation
import com.example.micardioat.domain.model.Paciente
import com.example.micardioat.domain.model.PacienteDetalle
import com.example.micardioat.domain.model.Visita

@Entity(tableName = "pacientes")
data class PacienteEntity(
    @PrimaryKey(autoGenerate = true)
    val pacienteId: Int? = null,
    val nombre: String,
    val edad: Int,
    val sexo: String = "",
    val antecedentesQuirurgicos: String = "",
    val antecedentesPatologicos: String = "",
    val alergias: String = ""
)

@Entity(
    tableName = "visitas",
    foreignKeys = [
        ForeignKey(
            entity = PacienteEntity::class,
            parentColumns = ["pacienteId"],
            childColumns = ["pacienteId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("pacienteId")]
)
data class VisitaEntity(
    @PrimaryKey(autoGenerate = true)
    val visitaId: Int? = null,
    val pacienteId: Int,
    val diagnostico: String = "",
    val presionArterial: String = "",
    val motivoConsulta: String = "",
    val fc: String = "",
    val fr: String = "",
    val tratamiento: String = "",
    val hb: String = "",
    val hct: String = "",
    val glicemia: String = "",
    val colTotal: String = "",
    val fevi: String = "",
    val plan: String = "",
    val fechaCita: Long? = null
)

data class PacienteConVisitas(
    @Embedded val paciente: PacienteEntity,
    @Relation(
        parentColumn = "pacienteId",
        entityColumn = "pacienteId"
    )
    val visitas: List<VisitaEntity>
)

fun PacienteEntity.toDomain(): Paciente {
    return Paciente(
        pacienteId = pacienteId,
        nombre = nombre,
        edad = edad,
        sexo = sexo,
        antecedentesQuirurgicos = antecedentesQuirurgicos,
        antecedentesPatologicos = antecedentesPatologicos,
        alergias = alergias
    )
}

fun Paciente.toEntity(): PacienteEntity {
    return PacienteEntity(
        pacienteId = pacienteId,
        nombre = nombre,
        edad = edad,
        sexo = sexo,
        antecedentesQuirurgicos = antecedentesQuirurgicos,
        antecedentesPatologicos = antecedentesPatologicos,
        alergias = alergias
    )
}

fun VisitaEntity.toDomain(): Visita {
    return Visita(
        visitaId = visitaId,
        pacienteId = pacienteId,
        diagnostico = diagnostico,
        presionArterial = presionArterial,
        motivoConsulta = motivoConsulta,
        fc = fc,
        fr = fr,
        tratamiento = tratamiento,
        hb = hb,
        hct = hct,
        glicemia = glicemia,
        colTotal = colTotal,
        fevi = fevi,
        plan = plan,
        fechaCita = fechaCita
    )
}

fun Visita.toEntity(): VisitaEntity {
    return VisitaEntity(
        visitaId = visitaId,
        pacienteId = pacienteId,
        diagnostico = diagnostico,
        presionArterial = presionArterial,
        motivoConsulta = motivoConsulta,
        fc = fc,
        fr = fr,
        tratamiento = tratamiento,
        hb = hb,
        hct = hct,
        glicemia = glicemia,
        colTotal = colTotal,
        fevi = fevi,
        plan = plan,
        fechaCita = fechaCita
    )
}

fun PacienteConVisitas.toDomain(): PacienteDetalle {
    return PacienteDetalle(
        paciente = paciente.toDomain(),
        visitas = visitas.map { it.toDomain() }
    )
}