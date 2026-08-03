package com.example.micardioat.presentation.paciente_list

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.micardioat.domain.model.PacienteCardiologia
import com.example.micardioat.domain.use_case.GetPacienteByIdUseCase
import com.example.micardioat.domain.use_case.SavePacienteUseCase
import com.example.micardioat.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PacienteAddViewModel @Inject constructor(
    private val savePacienteUseCase: SavePacienteUseCase,
    private val getPacienteByIdUseCase: GetPacienteByIdUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    var nombre by mutableStateOf("")
    var edad by mutableStateOf("")
    var diagnostico by mutableStateOf("")
    var presionArterial by mutableStateOf("")

    private var currentPacienteId: Int? = null

    init {
        savedStateHandle.get<Int>("pacienteId")?.let { pacienteId ->
            if (pacienteId != -1 && pacienteId != 0) {
                currentPacienteId = pacienteId
                getPaciente(pacienteId)
            }
        }
    }
    private fun getPaciente(id: Int) {
        viewModelScope.launch {
            getPacienteByIdUseCase(id).collect { result ->
                when (result) {
                    is Resource.Success -> {
                        result.data?.let { paciente ->
                            nombre = paciente.nombre
                            edad = paciente.edad.toString()
                            diagnostico = paciente.diagnostico
                            presionArterial = paciente.presionArterial
                        }
                    }
                    is Resource.Error -> {
                    }
                    is Resource.Loading -> {
                    }
                }
            }
        }
    }

    fun savePaciente(onSuccess: () -> Unit) {
        viewModelScope.launch {
            val nuevoPaciente = PacienteCardiologia(
                pacienteId = currentPacienteId,
                nombre = nombre,
                edad = edad.toIntOrNull() ?: 0,
                diagnostico = diagnostico,
                presionArterial = presionArterial
            )

            savePacienteUseCase(nuevoPaciente).collect {
                onSuccess()
            }
        }
    }
}