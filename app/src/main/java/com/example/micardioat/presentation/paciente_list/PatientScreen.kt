package com.example.micardioat.presentation.paciente_list

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.micardioat.domain.model.PacienteCardiologia
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PatientsScreen(
    onNavigateToDetail: (Int) -> Unit,
    onAddPatient: () -> Unit,
    viewModel: PacienteListViewModel = hiltViewModel()
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedFilter by remember { mutableStateOf("Pacientes") }

    val allPatients by viewModel.pacientes.collectAsState()
    val filters = listOf("Pacientes", "Recientes", "Riesgo Alto", "Chronico")
    val filteredAndSortedPatients = allPatients
        .filter { paciente ->
            val matchesSearch = paciente.nombre.contains(searchQuery, ignoreCase = true)

            val matchesFilter = when (selectedFilter) {
                "Riesgo Alto" -> {
                    val heartRate = paciente.fc.toIntOrNull() ?: 0
                    heartRate > 100 || paciente.diagnostico.contains("Critical", ignoreCase = true)
                }
                "Chronico" -> {
                    val diag = paciente.diagnostico
                    diag.contains("Chronic", ignoreCase = true) ||
                            diag.contains("Crónico", ignoreCase = true) ||
                            diag.contains("Crónica", ignoreCase = true)
                }
                else -> true
            }

            matchesSearch && matchesFilter
        }
        .let { filteredList ->
            when (selectedFilter) {
                "Recientes" -> {
                    filteredList.sortedByDescending { it.fechaCita ?: 0L }
                }
                else -> {
                    filteredList.sortedBy { it.nombre.lowercase() }
                }
            }
        }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddPatient,
                containerColor = Color(0xFF006D77),
                contentColor = Color.White,
                shape = RoundedCornerShape(16.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Patient")
            }
        },
        containerColor = Color(0xFFF8F9FA)
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFB2EBF2)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("", fontSize = 16.sp)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "CardioCare",
                        color = Color(0xFF006D77),
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                }
                Icon(
                    imageVector = Icons.Default.Notifications,
                    contentDescription = "Notifications",
                    tint = Color(0xFF006D77)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "Base de Datos de Pacientes",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF004D40)
            )

            Spacer(modifier = Modifier.height(12.dp))
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = {
                    Text("Buscar por nombre, ID o condición...", color = Color.Gray, fontSize = 14.sp)
                },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search", tint = Color.Gray) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF006D77),
                    unfocusedBorderColor = Color(0xFFE0E0E0),
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White
                ),
                shape = RoundedCornerShape(8.dp),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(filters) { filter ->
                    val isSelected = filter == selectedFilter
                    FilterChip(
                        selected = isSelected,
                        onClick = { selectedFilter = filter },
                        label = {
                            Text(
                                text = filter,
                                color = if (isSelected) Color(0xFF006D77) else Color.Gray,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                fontSize = 12.sp
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = Color(0xFFB2EBF2),
                            containerColor = Color.White
                        ),
                        border = FilterChipDefaults.filterChipBorder(
                            enabled = true,
                            selected = isSelected,
                            borderColor = if (isSelected) Color(0xFF006D77) else Color(0xFFE0E0E0)
                        ),
                        shape = RoundedCornerShape(16.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            if (filteredAndSortedPatients.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No se encontraron pacientes.", color = Color.Gray)
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(bottom = 80.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(filteredAndSortedPatients) { paciente ->
                        PatientDatabaseCard(
                            paciente = paciente,
                            onClick = { paciente.pacienteId?.let { onNavigateToDetail(it) } }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun PatientDatabaseCard(
    paciente: PacienteCardiologia,
    onClick: () -> Unit
) {
    val heartRate = paciente.fc.toIntOrNull() ?: 0
    val isCritical = heartRate > 100 || paciente.diagnostico.contains("Critical", ignoreCase = true)

    val statusText = if (isCritical) "Critical" else "Stable"
    val statusColor = if (isCritical) Color(0xFFD32F2F) else Color(0xFF006D77)
    val statusBgColor = if (isCritical) Color(0xFFFFEBEE) else Color(0xFFE0F2F1)
    val cardBorderColor = if (isCritical) Color(0xFFFFCDD2) else Color(0xFFE3EBF5)
    val lastVisitDate = paciente.fechaCita?.let {
        SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(Date(it))
    } ?: "N/A"

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, cardBorderColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                val initial = if (paciente.nombre.isNotBlank()) paciente.nombre.take(2).uppercase() else "??"
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFE3EBF5)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = initial, color = Color(0xFF004D40), fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = paciente.nombre,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = Color(0xFF1A1A1A)
                    )
                    Text(
                        text = "${paciente.sexo.ifEmpty { "Unknown" }}, ${paciente.edad} yrs",
                        color = Color.Gray,
                        fontSize = 12.sp
                    )
                }
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(statusBgColor)
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = statusText,
                        color = statusColor,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider(color = Color(0xFFF0F0F0), thickness = 1.dp)
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                Column {
                    Text(text = "LAST VISIT", color = Color.Gray, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = lastVisitDate, color = Color.Black, fontSize = 13.sp)
                }
                Column(horizontalAlignment = Alignment.End) {
                    Row(verticalAlignment = Alignment.Bottom) {
                        Text(text = "HR TREND", color = Color.Gray, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = "${paciente.fc.ifEmpty { "--" }} BPM",
                            color = statusColor,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    MiniSparkline(color = statusColor)
                }
            }
        }
    }
}

@Composable
fun MiniSparkline(color: Color) {
    Canvas(modifier = Modifier.width(70.dp).height(16.dp)) {
        val path = Path().apply {
            moveTo(0f, size.height * 0.7f)
            lineTo(size.width * 0.2f, size.height * 0.4f)
            lineTo(size.width * 0.4f, size.height * 0.8f)
            lineTo(size.width * 0.6f, size.height * 0.2f)
            lineTo(size.width * 0.8f, size.height * 0.6f)
            lineTo(size.width, size.height * 0.4f)
        }
        drawPath(
            path = path,
            color = color,
            style = Stroke(width = 3f)
        )
    }
}