package com.example.micardioat.domain.use_case

import com.example.micardioat.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class RegisterUseCase @Inject constructor() {

    operator fun invoke(usuario: String, clave: String, confirmarClave: String): Flow<Resource<Boolean>> = flow {
        emit(Resource.Loading())

        if (usuario.isBlank() || clave.isBlank() || confirmarClave.isBlank()) {
            emit(Resource.Error("Por favor completa todos los campos"))
            return@flow
        }

        if (clave != confirmarClave) {
            emit(Resource.Error("Las contraseñas no coinciden"))
            return@flow
        }

        emit(Resource.Success(true))
    }
}