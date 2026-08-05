package com.example.micardioat.presentation.paciente_list

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.micardioat.domain.model.PacienteCardiologia
import com.example.micardioat.domain.use_case.DeletePacienteUseCase
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
    private val deletePacienteUseCase: DeletePacienteUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    var currentPacienteId by mutableStateOf<Int?>(null)
    var isEditing by mutableStateOf(false)

    var nombre by mutableStateOf("")
    var edad by mutableStateOf("")
    var diagnostico by mutableStateOf("")
    var presionArterial by mutableStateOf("")

    var sexo by mutableStateOf("")
    var motivoConsulta by mutableStateOf("")
    var fc by mutableStateOf("")
    var fr by mutableStateOf("")
    var antecedentesQuirurgicos by mutableStateOf("")
    var antecedentesPatologicos by mutableStateOf("")
    var tratamiento by mutableStateOf("")
    var alergias by mutableStateOf("")
    var hb by mutableStateOf("")
    var hct by mutableStateOf("")
    var glicemia by mutableStateOf("")
    var colTotal by mutableStateOf("")
    var fevi by mutableStateOf("")
    var plan by mutableStateOf("")
    var fechaCita by mutableStateOf<Long?>(null)

    init {
        savedStateHandle.get<Int>("pacienteId")?.let { pacienteId ->
            if (pacienteId != -1 && pacienteId != 0) {
                currentPacienteId = pacienteId
                isEditing = true
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
                            sexo = paciente.sexo
                            motivoConsulta = paciente.motivoConsulta
                            fc = paciente.fc
                            fr = paciente.fr
                            antecedentesQuirurgicos = paciente.antecedentesQuirurgicos
                            antecedentesPatologicos = paciente.antecedentesPatologicos
                            tratamiento = paciente.tratamiento
                            alergias = paciente.alergias
                            hb = paciente.hb
                            hct = paciente.hct
                            glicemia = paciente.glicemia
                            colTotal = paciente.colTotal
                            fevi = paciente.fevi
                            plan = paciente.plan
                            fechaCita = paciente.fechaCita
                        }
                    }
                    is Resource.Error -> { }
                    is Resource.Loading -> { }
                }
            }
        }
    }

    fun savePaciente(onSuccess: () -> Unit) {
        viewModelScope.launch {
            val paciente = PacienteCardiologia(
                pacienteId = currentPacienteId,
                nombre = nombre,
                edad = edad.toIntOrNull() ?: 0,
                diagnostico = diagnostico,
                presionArterial = presionArterial,
                sexo = sexo,
                motivoConsulta = motivoConsulta,
                fc = fc,
                fr = fr,
                antecedentesQuirurgicos = antecedentesQuirurgicos,
                antecedentesPatologicos = antecedentesPatologicos,
                tratamiento = tratamiento,
                alergias = alergias,
                hb = hb,
                hct = hct,
                glicemia = glicemia,
                colTotal = colTotal,
                fevi = fevi,
                plan = plan,
                fechaCita = fechaCita
            )

            savePacienteUseCase(paciente).collect { result ->
                if (result is Resource.Success) {
                    onSuccess()
                }
            }
        }
    }

    fun deletePaciente(onSuccess: () -> Unit) {
        viewModelScope.launch {
            currentPacienteId?.let { id ->
                deletePacienteUseCase(id).collect { result ->
                    if (result is Resource.Success) {
                        onSuccess()
                    }
                }
            }
        }
    }
}