package com.example.micardioat.presentation.paciente_list

import com.example.micardioat.domain.model.PacienteDetalle

data class PacienteListUiState(
    val isLoading: Boolean = false,
    val pacientes: List<PacienteDetalle> = emptyList(),
    val message: String? = null,
    val navigateToCreate: Boolean = false,
    val navigateToEditId: Int? = null
)