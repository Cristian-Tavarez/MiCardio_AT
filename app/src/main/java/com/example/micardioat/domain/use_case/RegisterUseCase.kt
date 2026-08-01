package com.example.micardioat.domain.use_case

import com.example.micardioat.data.dao.UsuarioDao
import com.example.micardioat.data.entity.UsuarioEntity
import com.example.micardioat.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class RegisterUseCase @Inject constructor(
    private val usuarioDao: UsuarioDao
) {
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

        val usuarioExistente = usuarioDao.getUsuario(usuario.trim())
        if (usuarioExistente != null) {
            emit(Resource.Error("El nombre de usuario ya existe"))
            return@flow
        }

        usuarioDao.insertUsuario(UsuarioEntity(usuario = usuario.trim(), clave = clave))
        emit(Resource.Success(true))
    }.flowOn(Dispatchers.IO)
}