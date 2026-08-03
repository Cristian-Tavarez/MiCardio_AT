package com.example.micardioat.data.repository

import com.example.micardioat.data.dao.PacienteDao
import com.example.micardioat.data.entity.toDomain
import com.example.micardioat.data.entity.toEntity
import com.example.micardioat.domain.model.PacienteCardiologia
import com.example.micardioat.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PacienteCardiologiaRepository @Inject constructor(
    private val dao: PacienteDao
) {

    fun getPacientes(): Flow<Resource<List<PacienteCardiologia>>> {
        return dao.getPacientes().map { entities ->
            Resource.Success(entities.map { it.toDomain() })
        }
    }

    fun getPacienteById(id: Int): Flow<Resource<PacienteCardiologia>> = flow {
        try {
            val entity = dao.getPacienteById(id)
            if (entity != null) {
                emit(Resource.Success(entity.toDomain()))
            } else {
                emit(Resource.Error("Paciente no encontrado"))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Error desconocido"))
        }
    }

    fun savePaciente(paciente: PacienteCardiologia): Flow<Resource<Unit>> = flow {
        try {
            dao.insertPaciente(paciente.toEntity())
            emit(Resource.Success(Unit))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Error al guardar el paciente"))
        }
    }

    fun deletePaciente(id: Int): Flow<Resource<Unit>> = flow {
        try {
            dao.deletePacienteById(id)
            emit(Resource.Success(Unit))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Error al eliminar el paciente"))
        }
    }
}