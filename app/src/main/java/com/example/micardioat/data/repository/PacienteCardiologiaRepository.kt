package com.example.micardioat.data.repository

import com.example.micardioat.data.dao.PacienteDao
import com.example.micardioat.data.entity.toDomain
import com.example.micardioat.data.entity.toEntity
import com.example.micardioat.domain.model.Paciente
import com.example.micardioat.domain.model.PacienteDetalle
import com.example.micardioat.domain.model.Visita
import com.example.micardioat.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PacienteCardiologiaRepository @Inject constructor(
    private val dao: PacienteDao
) {

    fun getPacientes(): Flow<Resource<List<PacienteDetalle>>> {
        return dao.getAllPacientesConVisitas().map { entities ->
            Resource.Success(entities.map { it.toDomain() })
        }
    }

    fun getPacienteById(id: Int): Flow<Resource<PacienteDetalle>> = flow {
        try {
            val entity = dao.getPacienteConVisitas(id)
            if (entity != null) {
                emit(Resource.Success(entity.toDomain()))
            } else {
                emit(Resource.Error("Paciente no encontrado"))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Error desconocido"))
        }
    }

    fun savePacienteConVisita(paciente: Paciente, visita: Visita?): Flow<Resource<Unit>> = flow {
        try {
            val finalPacienteId = if (paciente.pacienteId != null && paciente.pacienteId != 0) {
                dao.updatePaciente(paciente.toEntity())
                paciente.pacienteId
            } else {
                dao.insertPaciente(paciente.toEntity()).toInt()
            }

            if (visita != null) {
                val visitaToSave = visita.copy(pacienteId = finalPacienteId)
                if (visitaToSave.visitaId != null && visitaToSave.visitaId != 0) {
                    dao.updateVisita(visitaToSave.toEntity())
                } else {
                    dao.insertVisita(visitaToSave.toEntity())
                }
            }

            emit(Resource.Success(Unit))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Error al guardar los datos"))
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