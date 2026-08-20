package com.example.micardioat.data.repository

import com.example.micardioat.utils.Resource
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth
) : AuthRepository {

    override suspend fun registerUser(email: String, clave: String): Resource<Boolean> {
        return try {
            auth.createUserWithEmailAndPassword(email, clave).await()
            Resource.Success(true)
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "Error al registrar el usuario")
        }
    }
}