package com.example.micardioat.domain.use_case

import com.example.micardioat.data.repository.PacienteCardiologiaRepository
import com.example.micardioat.domain.model.PacienteDetalle
import com.example.micardioat.utils.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPacienteByIdUseCase @Inject constructor(
    private val repository: PacienteCardiologiaRepository
) {
    operator fun invoke(id: Int): Flow<Resource<PacienteDetalle>> {
        return repository.getPacienteById(id)
    }
}