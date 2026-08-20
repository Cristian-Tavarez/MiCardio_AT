package com.example.micardioat.presentation.register

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.micardioat.data.repository.AuthRepository
import com.example.micardioat.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    var usuario by mutableStateOf("")
    var clave by mutableStateOf("")
    var confirmarClave by mutableStateOf("")

    private val _state = MutableStateFlow<Resource<Boolean>?>(null)
    val state: StateFlow<Resource<Boolean>?> = _state.asStateFlow()

    fun onEvent() {
        val email = usuario.trim()

        if (email.isBlank() || clave.isBlank() || confirmarClave.isBlank()) {
            _state.value = Resource.Error("Por favor completa todos los campos")
            return
        }

        if (clave != confirmarClave) {
            _state.value = Resource.Error("Las contraseñas no coinciden")
            return
        }

        if (clave.length < 6) {
            _state.value = Resource.Error("La contraseña debe tener al menos 6 caracteres")
            return
        }

        _state.value = Resource.Loading()

        viewModelScope.launch {
            _state.value = authRepository.registerUser(email, clave)
        }
    }
}