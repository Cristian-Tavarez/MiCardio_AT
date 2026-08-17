package com.example.micardioat.presentation.paciente_list

data class PacienteFormUiState(
    val pacienteId: Int? = null,
    val isNew: Boolean = true,
    val isLoading: Boolean = false,
    val isSaving: Boolean = false,
    val saved: Boolean = false,
    val isDeleting: Boolean = false,
    val deleted: Boolean = false,

    val nombre: String = "",
    val edad: String = "",
    val diagnostico: String = "",
    val presionArterial: String = "",
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
    val fechaCita: Long? = null,

    val nombreError: String? = null,
    val edadError: String? = null,
    val sexoError: String? = null,
    val motivoConsultaError: String? = null,
    val fechaCitaError: String? = null,
    val errorMessage: String? = null
)