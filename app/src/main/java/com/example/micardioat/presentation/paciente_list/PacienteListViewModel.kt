package com.example.micardioat.presentation.paciente_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.micardioat.domain.model.PacienteDetalle
import com.example.micardioat.domain.model.Visita
import com.example.micardioat.domain.use_case.GetPacientesUseCase
import com.example.micardioat.domain.use_case.SavePacienteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PacienteListViewModel @Inject constructor(
    getPacientesUseCase: GetPacientesUseCase,
    private val savePacienteUseCase: SavePacienteUseCase
) : ViewModel() {

    val pacientes: StateFlow<List<PacienteDetalle>> = getPacientesUseCase()
        .map { resource ->
            resource.data ?: emptyList()
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun quickScheduleVisit(detalle: PacienteDetalle, dateMillis: Long) {
        viewModelScope.launch {
            val ultimaVisita = detalle.visitas.maxByOrNull { it.visitaId ?: 0 }

            val nuevaVisita = ultimaVisita?.copy(
                visitaId = null,
                fechaCita = dateMillis
            ) ?: Visita(
                pacienteId = detalle.paciente.pacienteId ?: 0,
                fechaCita = dateMillis,
                motivoConsulta = "Cita de seguimiento"
            )

            savePacienteUseCase(detalle.paciente, nuevaVisita).collect { }
        }
    }
}