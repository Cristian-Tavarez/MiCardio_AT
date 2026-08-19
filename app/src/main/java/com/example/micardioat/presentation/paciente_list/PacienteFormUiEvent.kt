package com.example.micardioat.presentation.paciente_list

sealed interface PacienteFormUiEvent {
    data class Load(val id: Int?) : PacienteFormUiEvent

    data class NombreChanged(val value: String) : PacienteFormUiEvent

    data class ApellidoChanged(val value: String) : PacienteFormUiEvent
    data class EdadChanged(val value: String) : PacienteFormUiEvent
    data class DiagnosticoChanged(val value: String) : PacienteFormUiEvent
    data class PresionArterialChanged(val value: String) : PacienteFormUiEvent
    data class SexoChanged(val value: String) : PacienteFormUiEvent
    data class MotivoConsultaChanged(val value: String) : PacienteFormUiEvent
    data class FcChanged(val value: String) : PacienteFormUiEvent
    data class FrChanged(val value: String) : PacienteFormUiEvent
    data class AntecedentesQuirurgicosChanged(val value: String) : PacienteFormUiEvent
    data class AntecedentesPatologicosChanged(val value: String) : PacienteFormUiEvent
    data class TratamientoChanged(val value: String) : PacienteFormUiEvent
    data class AlergiasChanged(val value: String) : PacienteFormUiEvent
    data class HbChanged(val value: String) : PacienteFormUiEvent
    data class HctChanged(val value: String) : PacienteFormUiEvent
    data class GlicemiaChanged(val value: String) : PacienteFormUiEvent
    data class ColTotalChanged(val value: String) : PacienteFormUiEvent
    data class FeviChanged(val value: String) : PacienteFormUiEvent
    data class PlanChanged(val value: String) : PacienteFormUiEvent
    data class FechaCitaChanged(val value: Long?) : PacienteFormUiEvent

    data object Save : PacienteFormUiEvent
    data object Delete : PacienteFormUiEvent
}