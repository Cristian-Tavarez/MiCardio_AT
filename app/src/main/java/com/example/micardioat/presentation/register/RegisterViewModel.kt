package com.example.micardioat.presentation.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.micardioat.utils.Resource
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val auth: FirebaseAuth
) : ViewModel() {

    private val _state = MutableStateFlow(RegisterUiState())
    val state: StateFlow<RegisterUiState> = _state.asStateFlow()

    fun onEvent(event: RegisterUiEvent) {
        when (event) {
            is RegisterUiEvent.UsuarioChanged -> {
                _state.update {
                    it.copy(usuario = event.value)
                }
            }

            is RegisterUiEvent.ClaveChanged -> {
                _state.update {
                    it.copy(clave = event.value)
                }
            }

            is RegisterUiEvent.ConfirmarClaveChanged -> {
                _state.update {
                    it.copy(confirmarClave = event.value)
                }
            }

            RegisterUiEvent.Register -> {
                onRegister()
            }
        }
    }

    private fun onRegister() {
        val currentState = state.value
        val email = currentState.usuario.trim()
        val clave = currentState.clave
        val confirmarClave = currentState.confirmarClave

        if (
            email.isBlank() ||
            clave.isBlank() ||
            confirmarClave.isBlank()
        ) {
            _state.update {
                it.copy(
                    registerState =
                        Resource.Error("Por favor completa todos los campos")
                )
            }
            return
        }

        if (clave != confirmarClave) {
            _state.update {
                it.copy(
                    registerState =
                        Resource.Error("Las contraseñas no coinciden")
                )
            }
            return
        }

        if (clave.length < 6) {
            _state.update {
                it.copy(
                    registerState =
                        Resource.Error(
                            "La contraseña debe tener al menos 6 caracteres"
                        )
                )
            }
            return
        }

        _state.update {
            it.copy(registerState = Resource.Loading())
        }

        viewModelScope.launch {
            auth.createUserWithEmailAndPassword(email, clave)
                .addOnSuccessListener {
                    _state.update {
                        it.copy(
                            registerState = Resource.Success(true)
                        )
                    }
                }
                .addOnFailureListener { exception ->
                    _state.update {
                        it.copy(
                            registerState = Resource.Error(
                                exception.localizedMessage
                                    ?: "Error al registrar el usuario"
                            )
                        )
                    }
                }
        }
    }
}