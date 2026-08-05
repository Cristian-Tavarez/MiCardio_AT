package com.example.micardioat.domain.model

data class PacienteCardiologia(
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
    val plan: String = "",
    val fechaCita: Long? = null
)