package com.example.micardioat.presentation.paciente_list

import com.example.micardioat.domain.model.Visita

data class PacienteFormUiState(
    val isLoading: Boolean = false,
    val isSaving: Boolean = false,
    val isDeleting: Boolean = false,
    val saved: Boolean = false,
    val deleted: Boolean = false,
    val isNew: Boolean = true,
    val errorMessage: String? = null,

    val pacienteId: Int? = null,
    val nombre: String = "",
    val apellido: String = "",
    val edad: String = "",
    val sexo: String = "",
    val antecedentesQuirurgicos: String = "",
    val antecedentesPatologicos: String = "",
    val alergias: String = "",

    val visitaId: Int? = null,
    val originalFechaCita: Long? = null,

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
    val fechaCita: Long? = null,

    val nombreError: String? = null,
    val apellidoError: String? = null,
    val edadError: String? = null,
    val sexoError: String? = null,
    val motivoConsultaError: String? = null,
    val fechaCitaError: String? = null,

    val historialVisitas: List<Visita> = emptyList()
)