package com.example.micardioat.presentation.paciente_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.micardioat.domain.model.PacienteCardiologia
import com.example.micardioat.domain.use_case.GetPacientesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class PacienteListViewModel @Inject constructor(
    getPacientesUseCase: GetPacientesUseCase
) : ViewModel() {
    val pacientes: StateFlow<List<PacienteCardiologia>> = getPacientesUseCase()
        .map { resource ->
            resource.data ?: emptyList()
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
}