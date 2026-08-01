package com.example.micardioat.domain.use_case

import com.example.micardioat.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class LoginUseCase @Inject constructor() {

    operator fun invoke(usuario: String, clave: String): Flow<Resource<Boolean>> = flow {
        emit(Resource.Loading())

        if (usuario.isBlank() || clave.isBlank()) {
            emit(Resource.Error("El usuario y la contraseña no pueden estar vacíos"))
            return@flow
        }

        if (usuario == "admin" && clave == "1234") {
            emit(Resource.Success(true))
        } else {
            emit(Resource.Error("Usuario o contraseña incorrectos"))
        }
    }
}