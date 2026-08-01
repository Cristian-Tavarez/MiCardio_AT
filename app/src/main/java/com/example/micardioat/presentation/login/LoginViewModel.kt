package com.example.micardioat.presentation.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.micardioat.domain.use_case.LoginUseCase
import com.example.micardioat.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase
) : ViewModel() {

    var usuario by mutableStateOf("")
    var clave by mutableStateOf("")

    private val _loginState = MutableStateFlow<Resource<Boolean>?>(null)
    val loginState: StateFlow<Resource<Boolean>?> = _loginState.asStateFlow()

    fun onLogin() {
        viewModelScope.launch {
            loginUseCase(usuario, clave).collect { result ->
                _loginState.value = result
            }
        }
    }
}