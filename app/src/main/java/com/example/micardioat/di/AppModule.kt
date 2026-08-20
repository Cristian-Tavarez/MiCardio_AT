package com.example.micardioat.di

import android.content.Context
import androidx.room.Room
import com.example.micardioat.data.dao.PacienteDao
import com.example.micardioat.data.dao.UsuarioDao
import com.example.micardioat.data.database.AppDatabase
import com.example.micardioat.data.repository.AuthRepository
import com.example.micardioat.data.repository.AuthRepositoryImpl
import com.example.micardioat.data.repository.PacienteCardiologiaRepository
import com.example.micardioat.domain.use_case.*
import com.example.micardioat.utils.ThemePreferences
import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "micardioat_db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideUsuarioDao(appDatabase: AppDatabase): UsuarioDao {
        return appDatabase.usuarioDao()
    }

    @Provides
    @Singleton
    fun providePacienteDao(appDatabase: AppDatabase): PacienteDao {
        return appDatabase.pacienteDao()
    }

    @Provides
    @Singleton
    fun providePacienteCardiologiaRepository(dao: PacienteDao): PacienteCardiologiaRepository {
        return PacienteCardiologiaRepository(dao)
    }

    @Provides
    @Singleton
    fun provideAuthRepository(auth: FirebaseAuth): AuthRepository {
        return AuthRepositoryImpl(auth)
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

    @Provides
    @Singleton
    fun provideLoginUseCase(usuarioDao: UsuarioDao): LoginUseCase {
        return LoginUseCase(usuarioDao)
    }

    @Provides
    @Singleton
    fun provideRegisterUseCase(usuarioDao: UsuarioDao): RegisterUseCase {
        return RegisterUseCase(usuarioDao)
    }

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }

    @Provides
    @Singleton
    fun provideThemePreferences(@ApplicationContext context: Context): ThemePreferences {
        return ThemePreferences(context)
    }
}