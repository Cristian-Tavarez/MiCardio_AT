package com.example.micardioat.domain.use_case

import com.example.micardioat.data.repository.PacienteCardiologiaRepository
import com.example.micardioat.domain.model.PacienteCardiologia
import com.example.micardioat.utils.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SavePacienteUseCase @Inject constructor(
    private val repository: PacienteCardiologiaRepository
) {
    operator fun invoke(paciente: PacienteCardiologia): Flow<Resource<Unit>> {
        return repository.savePaciente(paciente)
    }
}