package com.example.micardioat.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.micardioat.data.entity.PacienteConVisitas
import com.example.micardioat.data.entity.PacienteEntity
import com.example.micardioat.data.entity.VisitaEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PacienteDao {
    @Query("SELECT * FROM pacientes")
    fun getPacientes(): Flow<List<PacienteEntity>>

    @Query("SELECT * FROM pacientes WHERE pacienteId = :id")
    suspend fun getPacienteById(id: Int): PacienteEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPaciente(paciente: PacienteEntity): Long

    // NEW: Explicit update to prevent deleting history
    @Update
    suspend fun updatePaciente(paciente: PacienteEntity)

    @Query("DELETE FROM pacientes WHERE pacienteId = :id")
    suspend fun deletePacienteById(id: Int)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVisita(visita: VisitaEntity): Long

    @Update
    suspend fun updateVisita(visita: VisitaEntity)

    @Query("DELETE FROM visitas WHERE visitaId = :id")
    suspend fun deleteVisitaById(id: Int)

    @Transaction
    @Query("SELECT * FROM pacientes WHERE pacienteId = :pacienteId")
    suspend fun getPacienteConVisitas(pacienteId: Int): PacienteConVisitas?

    @Transaction
    @Query("SELECT * FROM pacientes")
    fun getAllPacientesConVisitas(): Flow<List<PacienteConVisitas>>
}