package com.example.micardioat.presentation.paciente_list

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.example.micardioat.domain.model.PacienteCardiologia
import com.example.micardioat.domain.use_case.DeletePacienteUseCase
import com.example.micardioat.domain.use_case.GetPacienteByIdUseCase
import com.example.micardioat.domain.use_case.SavePacienteUseCase
import com.example.micardioat.presentation.navigation.Screen
import com.example.micardioat.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PacienteAddViewModel @Inject constructor(
    private val getPacienteByIdUseCase: GetPacienteByIdUseCase,
    private val savePacienteUseCase: SavePacienteUseCase,
    private val deletePacienteUseCase: DeletePacienteUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val routeArgs = savedStateHandle.toRoute<Screen.PacienteEdit>()
    private val pacienteId: Int? = routeArgs.pacienteId

    private val _state = MutableStateFlow(PacienteFormUiState())
    val state: StateFlow<PacienteFormUiState> = _state.asStateFlow()

    init {
        loadPaciente(pacienteId)
    }

    fun onEvent(event: PacienteFormUiEvent) {
        when (event) {
            is PacienteFormUiEvent.Load -> loadPaciente(event.id)
            is PacienteFormUiEvent.NombreChanged -> _state.update {
                it.copy(nombre = event.value, nombreError = null)
            }
            is PacienteFormUiEvent.EdadChanged -> _state.update {
                it.copy(edad = event.value, edadError = null)
            }
            is PacienteFormUiEvent.DiagnosticoChanged -> _state.update {
                it.copy(diagnostico = event.value)
            }
            is PacienteFormUiEvent.PresionArterialChanged -> _state.update {
                it.copy(presionArterial = event.value)
            }
            is PacienteFormUiEvent.SexoChanged -> _state.update {
                it.copy(sexo = event.value, sexoError = null)
            }
            is PacienteFormUiEvent.MotivoConsultaChanged -> _state.update {
                it.copy(motivoConsulta = event.value, motivoConsultaError = null)
            }
            is PacienteFormUiEvent.FcChanged -> _state.update { it.copy(fc = event.value) }
            is PacienteFormUiEvent.FrChanged -> _state.update { it.copy(fr = event.value) }
            is PacienteFormUiEvent.AntecedentesQuirurgicosChanged -> _state.update { it.copy(antecedentesQuirurgicos = event.value) }
            is PacienteFormUiEvent.AntecedentesPatologicosChanged -> _state.update { it.copy(antecedentesPatologicos = event.value) }
            is PacienteFormUiEvent.TratamientoChanged -> _state.update { it.copy(tratamiento = event.value) }
            is PacienteFormUiEvent.AlergiasChanged -> _state.update { it.copy(alergias = event.value) }
            is PacienteFormUiEvent.HbChanged -> _state.update { it.copy(hb = event.value) }
            is PacienteFormUiEvent.HctChanged -> _state.update { it.copy(hct = event.value) }
            is PacienteFormUiEvent.GlicemiaChanged -> _state.update { it.copy(glicemia = event.value) }
            is PacienteFormUiEvent.ColTotalChanged -> _state.update { it.copy(colTotal = event.value) }
            is PacienteFormUiEvent.FeviChanged -> _state.update { it.copy(fevi = event.value) }
            is PacienteFormUiEvent.PlanChanged -> _state.update { it.copy(plan = event.value) }
            is PacienteFormUiEvent.FechaCitaChanged -> _state.update {
                it.copy(fechaCita = event.value, fechaCitaError = null)
            }

            PacienteFormUiEvent.Save -> onSave()
            PacienteFormUiEvent.Delete -> onDelete()
        }
    }

    private fun loadPaciente(id: Int?) {
        if (id == null || id == 0 || id == -1) {
            _state.update { it.copy(isNew = true, pacienteId = null) }
            return
        }

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            getPacienteByIdUseCase(id).collect { result ->
                when (result) {
                    is Resource.Success -> {
                        result.data?.let { paciente ->
                            _state.update {
                                it.copy(
                                    isLoading = false,
                                    isNew = false,
                                    pacienteId = paciente.pacienteId,
                                    nombre = paciente.nombre,
                                    edad = paciente.edad.toString(),
                                    diagnostico = paciente.diagnostico,
                                    presionArterial = paciente.presionArterial,
                                    sexo = paciente.sexo,
                                    motivoConsulta = paciente.motivoConsulta,
                                    fc = paciente.fc,
                                    fr = paciente.fr,
                                    antecedentesQuirurgicos = paciente.antecedentesQuirurgicos,
                                    antecedentesPatologicos = paciente.antecedentesPatologicos,
                                    tratamiento = paciente.tratamiento,
                                    alergias = paciente.alergias,
                                    hb = paciente.hb,
                                    hct = paciente.hct,
                                    glicemia = paciente.glicemia,
                                    colTotal = paciente.colTotal,
                                    fevi = paciente.fevi,
                                    plan = paciente.plan,
                                    fechaCita = paciente.fechaCita
                                )
                            }
                        }
                    }
                    is Resource.Error -> {
                        _state.update { it.copy(isLoading = false, errorMessage = result.message) }
                    }
                    is Resource.Loading -> {
                        _state.update { it.copy(isLoading = true) }
                    }
                }
            }
        }
    }

    private fun onSave() {
        val s = state.value

        val nombreError = if (s.nombre.isBlank()) "El nombre es obligatorio" else null
        val edadError = if (s.edad.isBlank()) "La edad es obligatoria" else null
        val sexoError = if (s.sexo.isBlank()) "El sexo es obligatorio" else null
        val motivoError = if (s.motivoConsulta.isBlank()) "El motivo es obligatorio" else null
        val fechaError = if (s.fechaCita == null) "Debe seleccionar una fecha" else null

        if (nombreError != null || edadError != null || sexoError != null || motivoError != null || fechaError != null) {
            _state.update {
                it.copy(
                    nombreError = nombreError,
                    edadError = edadError,
                    sexoError = sexoError,
                    motivoConsultaError = motivoError,
                    fechaCitaError = fechaError
                )
            }
            return
        }

        viewModelScope.launch {
            _state.update { it.copy(isSaving = true) }

            val paciente = PacienteCardiologia(
                pacienteId = s.pacienteId,
                nombre = s.nombre.trim(),
                edad = s.edad.trim().toIntOrNull() ?: 0,
                diagnostico = s.diagnostico,
                presionArterial = s.presionArterial,
                sexo = s.sexo.trim(),
                motivoConsulta = s.motivoConsulta.trim(),
                fc = s.fc,
                fr = s.fr,
                antecedentesQuirurgicos = s.antecedentesQuirurgicos,
                antecedentesPatologicos = s.antecedentesPatologicos,
                tratamiento = s.tratamiento,
                alergias = s.alergias,
                hb = s.hb,
                hct = s.hct,
                glicemia = s.glicemia,
                colTotal = s.colTotal,
                fevi = s.fevi,
                plan = s.plan,
                fechaCita = s.fechaCita
            )

            savePacienteUseCase(paciente).collect { result ->
                when (result) {
                    is Resource.Success -> {
                        _state.update {
                            it.copy(
                                isSaving = false,
                                saved = true,
                                isNew = false
                            )
                        }
                    }
                    is Resource.Error -> {
                        _state.update {
                            it.copy(
                                isSaving = false,
                                errorMessage = result.message ?: "Ocurrió un error al guardar"
                            )
                        }
                    }
                    is Resource.Loading -> { }
                }
            }
        }
    }

    private fun onDelete() {
        val id = state.value.pacienteId ?: return
        viewModelScope.launch {
            _state.update { it.copy(isDeleting = true) }
            deletePacienteUseCase(id).collect { result ->
                if (result is Resource.Success) {
                    _state.update { it.copy(isDeleting = false, deleted = true) }
                } else {
                    _state.update { it.copy(isDeleting = false) }
                }
            }
        }
    }
}