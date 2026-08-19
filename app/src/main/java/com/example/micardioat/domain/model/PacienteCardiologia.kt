package com.example.micardioat.domain.model

data class Paciente(
    val pacienteId: Int? = null,
    val nombre: String,
    val apellido: String = "",
    val edad: Int,
    val sexo: String = "",
    val antecedentesQuirurgicos: String = "",
    val antecedentesPatologicos: String = "",
    val alergias: String = ""
)

data class Visita(
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

data class PacienteDetalle(
    val paciente: Paciente,
    val visitas: List<Visita>
)