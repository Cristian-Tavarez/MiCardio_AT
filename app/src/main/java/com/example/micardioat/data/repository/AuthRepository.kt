package com.example.micardioat.data.repository

import com.example.micardioat.utils.Resource

interface AuthRepository {
    suspend fun registerUser(email: String, clave: String): Resource<Boolean>
}