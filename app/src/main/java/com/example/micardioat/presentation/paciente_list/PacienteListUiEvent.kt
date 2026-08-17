package com.example.micardioat.presentation.paciente_list

sealed interface PacienteListUiEvent {
    data object Load : PacienteListUiEvent
    data object Refresh : PacienteListUiEvent
    data class Delete(val id: Int) : PacienteListUiEvent
    data class ShowMessage(val message: String) : PacienteListUiEvent
    data object ClearMessage : PacienteListUiEvent
    data object CreateNew : PacienteListUiEvent
    data class Edit(val id: Int) : PacienteListUiEvent
}