package com.example.micardioat.presentation.paciente_list

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.micardioat.domain.model.PacienteCardiologia

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PacienteListScreen(
    viewModel: PacienteListViewModel = hiltViewModel(),
    onNavigateToDetail: (Int?) -> Unit = {}
) {
    val pacientes by viewModel.pacientes.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Pacientes de Cardiología", color = Color.Black, fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { onNavigateToDetail(null) },
                containerColor = Color(0xFFA5C0FF),
                contentColor = Color.White
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Agregar Paciente"
                )
            }
        }
    ) { paddingValues ->
        if (pacientes.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .background(Color.White),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No hay pacientes registrados",
                    color = Color.Gray
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .background(Color.White),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(pacientes) { paciente ->
                    PacienteItem(
                        paciente = paciente,
                        onClick = {
                            onNavigateToDetail(paciente.pacienteId)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun PacienteItem(
    paciente: PacienteCardiologia,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF13131A))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Text(
                text = paciente.nombre,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "Diagnóstico: ${if (paciente.diagnostico.isNotBlank()) paciente.diagnostico else paciente.motivoConsulta}",
                fontSize = 14.sp,
                color = Color(0xFFE0E0E0)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Edad: ${paciente.edad} | Presión: ${paciente.presionArterial}",
                fontSize = 12.sp,
                color = Color(0xFFA0A0A0)
            )
        }
    }
}