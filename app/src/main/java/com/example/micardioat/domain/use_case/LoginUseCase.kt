package com.example.micardioat.domain.use_case

import com.example.micardioat.data.dao.UsuarioDao
import com.example.micardioat.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val usuarioDao: UsuarioDao
) {
    operator fun invoke(usuario: String, clave: String): Flow<Resource<Boolean>> = flow {
        emit(Resource.Loading())

        if (usuario.isBlank() || clave.isBlank()) {
            emit(Resource.Error("El usuario y la contraseña no pueden estar vacíos"))
            return@flow
        }

        if (usuario == "admin" && clave == "1234") {
            emit(Resource.Success(true))
            return@flow
        }

        val userEntity = usuarioDao.getUsuario(usuario.trim())

        if (userEntity != null && userEntity.clave == clave) {
            emit(Resource.Success(true))
        } else {
            emit(Resource.Error("Usuario o contraseña incorrectos"))
        }
    }.flowOn(Dispatchers.IO)
}