package com.example.micardioat.data.repository

import com.example.micardioat.domain.model.PacienteCardiologia
import com.example.micardioat.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import javax.inject.Inject

class PacienteCardiologiaRepository @Inject constructor() {

    fun getPacientes(): Flow<Resource<List<PacienteCardiologia>>> {
        return emptyFlow()
    }

    fun getPacienteById(id: Int): Flow<Resource<PacienteCardiologia>> {
        return emptyFlow()
    }

    fun savePaciente(paciente: PacienteCardiologia): Flow<Resource<Unit>> {
        return emptyFlow()
    }

    fun deletePaciente(id: Int): Flow<Resource<Unit>> {
        return emptyFlow()
    }
}