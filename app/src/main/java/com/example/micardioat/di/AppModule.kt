package com.example.micardioat.di

import com.example.micardioat.data.repository.PacienteCardiologiaRepository
import com.example.micardioat.domain.use_case.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun providePacienteCardiologiaRepository(): PacienteCardiologiaRepository {
        return PacienteCardiologiaRepository()
    }

    @Provides
    @Singleton
    fun provideGetPacientesUseCase(repository: PacienteCardiologiaRepository): GetPacientesUseCase {
        return GetPacientesUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideGetPacienteByIdUseCase(repository: PacienteCardiologiaRepository): GetPacienteByIdUseCase {
        return GetPacienteByIdUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideSavePacienteUseCase(repository: PacienteCardiologiaRepository): SavePacienteUseCase {
        return SavePacienteUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideDeletePacienteUseCase(repository: PacienteCardiologiaRepository): DeletePacienteUseCase {
        return DeletePacienteUseCase(repository)
    }
}