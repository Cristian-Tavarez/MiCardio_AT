package com.example.micardioat.presentation.register

import com.example.micardioat.utils.Resource

data class RegisterUiState(
    val usuario: String = "",
    val clave: String = "",
    val confirmarClave: String = "",
    val registerState: Resource<Boolean>? = null
)