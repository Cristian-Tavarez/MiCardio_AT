package com.example.micardioat.presentation.paciente_list

import com.example.micardioat.domain.model.PacienteCardiologia

data class PacienteListUiState(
    val isLoading: Boolean = false,
    val pacientes: List<PacienteCardiologia> = emptyList(),
    val message: String? = null,
    val navigateToCreate: Boolean = false,
    val navigateToEditId: Int? = null
)