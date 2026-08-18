package com.example.micardioat.domain.use_case

import com.example.micardioat.data.repository.PacienteCardiologiaRepository
import com.example.micardioat.domain.model.Paciente
import com.example.micardioat.domain.model.Visita
import com.example.micardioat.utils.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SavePacienteUseCase @Inject constructor(
    private val repository: PacienteCardiologiaRepository
) {
    operator fun invoke(paciente: Paciente, visita: Visita? = null): Flow<Resource<Unit>> {
        return repository.savePacienteConVisita(paciente, visita)
    }
}