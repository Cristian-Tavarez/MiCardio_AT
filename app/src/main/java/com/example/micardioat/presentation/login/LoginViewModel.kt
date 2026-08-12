package com.example.micardioat.presentation.login

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
class LoginViewModel @Inject constructor(
    private val auth: FirebaseAuth
) : ViewModel() {

    var usuario by mutableStateOf("")
    var clave by mutableStateOf("")

    private val _loginState = MutableStateFlow<Resource<Boolean>?>(null)
    val loginState: StateFlow<Resource<Boolean>?> = _loginState.asStateFlow()

    fun isUserLoggedIn(): Boolean = auth.currentUser != null

    fun onLogin() {
        if (usuario.isBlank() || clave.isBlank()) {
            _loginState.value = Resource.Error("Por favor ingresa usuario y contraseña")
            return
        }

        _loginState.value = Resource.Loading()

        auth.signInWithEmailAndPassword(usuario.trim(), clave)
            .addOnSuccessListener {
                _loginState.value = Resource.Success(true)
            }
            .addOnFailureListener { exception ->
                _loginState.value = Resource.Error(
                    exception.localizedMessage ?: "Error de autenticación"
                )
            }
    }
}