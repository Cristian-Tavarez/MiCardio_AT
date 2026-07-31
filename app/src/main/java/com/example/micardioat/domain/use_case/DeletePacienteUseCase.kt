package com.example.micardioat.domain.use_case

import com.example.micardioat.data.repository.PacienteCardiologiaRepository
import com.example.micardioat.utils.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DeletePacienteUseCase @Inject constructor(
    private val repository: PacienteCardiologiaRepository
) {
    operator fun invoke(id: Int): Flow<Resource<Unit>> {
        return repository.deletePaciente(id)
    }
}