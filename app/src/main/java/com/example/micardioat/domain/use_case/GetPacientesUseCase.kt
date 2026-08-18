package com.example.micardioat.domain.use_case

import com.example.micardioat.data.repository.PacienteCardiologiaRepository
import com.example.micardioat.domain.model.PacienteDetalle
import com.example.micardioat.utils.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPacientesUseCase @Inject constructor(
    private val repository: PacienteCardiologiaRepository
) {
    operator fun invoke(): Flow<Resource<List<PacienteDetalle>>> {
        return repository.getPacientes()
    }
}