package com.example.micardioat.presentation.register

sealed interface RegisterUiEvent {

    data class UsuarioChanged(
        val value: String
    ) : RegisterUiEvent

    data class ClaveChanged(
        val value: String
    ) : RegisterUiEvent

    data class ConfirmarClaveChanged(
        val value: String
    ) : RegisterUiEvent

    data object Register : RegisterUiEvent
}