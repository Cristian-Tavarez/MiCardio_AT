package com.example.micardioat.presentation.paciente_list

import androidx.lifecycle.ViewModel
import com.example.micardioat.domain.use_case.GetPacientesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PacienteListViewModel @Inject constructor(
    private val getPacientesUseCase: GetPacientesUseCase
) : ViewModel() {

}