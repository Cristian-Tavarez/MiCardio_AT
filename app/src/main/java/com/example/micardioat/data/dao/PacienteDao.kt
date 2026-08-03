package com.example.micardioat.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.micardioat.data.entity.PacienteEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PacienteDao {
    @Query("SELECT * FROM pacientes")
    fun getPacientes(): Flow<List<PacienteEntity>>

    @Query("SELECT * FROM pacientes WHERE pacienteId = :id")
    suspend fun getPacienteById(id: Int): PacienteEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPaciente(paciente: PacienteEntity)

    @Query("DELETE FROM pacientes WHERE pacienteId = :id")
    suspend fun deletePacienteById(id: Int)
}