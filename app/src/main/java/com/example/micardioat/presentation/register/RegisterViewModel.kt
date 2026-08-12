package com.example.micardioat.presentation.register

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.micardioat.utils.Resource
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val auth: FirebaseAuth
) : ViewModel() {

    var usuario by mutableStateOf("")
    var clave by mutableStateOf("")
    var confirmarClave by mutableStateOf("")

    private val _registerState = MutableStateFlow<Resource<Boolean>?>(null)
    val registerState: StateFlow<Resource<Boolean>?> = _registerState.asStateFlow()

    fun onRegister() {
        val email = usuario.trim()

        if (email.isBlank() || clave.isBlank() || confirmarClave.isBlank()) {
            _registerState.value = Resource.Error("Por favor completa todos los campos")
            return
        }

        if (clave != confirmarClave) {
            _registerState.value = Resource.Error("Las contraseñas no coinciden")
            return
        }

        if (clave.length < 6) {
            _registerState.value = Resource.Error("La contraseña debe tener al menos 6 caracteres")
            return
        }

        _registerState.value = Resource.Loading()

        auth.createUserWithEmailAndPassword(email, clave)
            .addOnSuccessListener {
                _registerState.value = Resource.Success(true)
            }
            .addOnFailureListener { exception ->
                _registerState.value = Resource.Error(
                    exception.localizedMessage ?: "Error al registrar el usuario"
                )
            }
    }
}