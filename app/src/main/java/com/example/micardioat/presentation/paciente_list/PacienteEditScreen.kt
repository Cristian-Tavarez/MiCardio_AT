package com.example.micardioat.presentation.paciente_list

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.micardioat.domain.model.Visita
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun PacienteEditScreen(
    viewModel: PacienteAddViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit = {}
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    PacienteEditBody(
        state = state,
        onEvent = viewModel::onEvent,
        onNavigateBack = onNavigateBack
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PacienteEditBody(
    state: PacienteFormUiState,
    onEvent: (PacienteFormUiEvent) -> Unit,
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current
    val primaryColor = MaterialTheme.colorScheme.primary
    val onPrimaryColor = MaterialTheme.colorScheme.onPrimary
    val backgroundColor = MaterialTheme.colorScheme.background
    val surfaceColor = MaterialTheme.colorScheme.surface
    val onSurfaceColor = MaterialTheme.colorScheme.onSurface
    val onSurfaceVariantColor = MaterialTheme.colorScheme.onSurfaceVariant
    val outlineColor = MaterialTheme.colorScheme.outlineVariant
    val errorColor = MaterialTheme.colorScheme.error

    var isFormEditable by remember(state.isNew) { mutableStateOf(state.isNew) }
    var showDeleteDialog by remember { mutableStateOf(false) }

    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val tabs = listOf("Consulta Actual", "Historial (${state.historialVisitas.size})")

    LaunchedEffect(state.saved, state.deleted) {
        if (state.saved) {
            Toast.makeText(
                context,
                if (!state.isNew) "Visita registrada/actualizada" else "Paciente guardado",
                Toast.LENGTH_SHORT
            ).show()
            onNavigateBack()
        }
        if (state.deleted) {
            Toast.makeText(context, "Paciente eliminado correctamente", Toast.LENGTH_SHORT).show()
            onNavigateBack()
        }
    }

    LaunchedEffect(state.errorMessage) {
        state.errorMessage?.let { error ->
            Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
        }
    }

    val inputColors = OutlinedTextFieldDefaults.colors(
        focusedTextColor = onSurfaceColor,
        unfocusedTextColor = onSurfaceColor,
        focusedLabelColor = primaryColor,
        unfocusedLabelColor = onSurfaceVariantColor,
        focusedBorderColor = primaryColor,
        unfocusedBorderColor = outlineColor,
        cursorColor = primaryColor,
        disabledTextColor = onSurfaceColor.copy(alpha = 0.6f),
        disabledBorderColor = outlineColor.copy(alpha = 0.5f),
        disabledLabelColor = onSurfaceVariantColor.copy(alpha = 0.6f),
        disabledLeadingIconColor = onSurfaceVariantColor.copy(alpha = 0.6f)
    )

    var showDatePicker by remember { mutableStateOf(false) }

    val dateFormatter = remember {
        SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).apply {
            timeZone = java.util.TimeZone.getTimeZone("UTC")
        }
    }
    val dateString = state.fechaCita?.let { dateFormatter.format(Date(it)) } ?: "Seleccionar fecha"

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = {
                Text("Eliminar Paciente", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            },
            text = {
                Text("¿Estás seguro de que deseas eliminar este paciente y todo su historial?", fontSize = 15.sp)
            },
            confirmButton = {
                TextButton(onClick = {
                    showDeleteDialog = false
                    onEvent(PacienteFormUiEvent.Delete)
                }) {
                    Text("Sí", color = errorColor, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("No", color = onSurfaceVariantColor, fontWeight = FontWeight.Medium, fontSize = 16.sp)
                }
            },
            containerColor = surfaceColor,
            titleContentColor = onSurfaceColor,
            textContentColor = onSurfaceVariantColor
        )
    }

    if (showDatePicker && isFormEditable) {
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = state.fechaCita ?: System.currentTimeMillis()
        )

        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    onEvent(PacienteFormUiEvent.FechaCitaChanged(datePickerState.selectedDateMillis))
                    showDatePicker = false
                }) {
                    Text("Aceptar", color = primaryColor)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("Cancelar", color = onSurfaceVariantColor)
                }
            },
            colors = DatePickerDefaults.colors(containerColor = surfaceColor)
        ) {
            DatePicker(state = datePickerState)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (!state.isNew) "Ficha Médica" else "Registrar Paciente",
                        color = onSurfaceColor,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver", tint = onSurfaceColor)
                    }
                },
                actions = {
                    if (!state.isNew) {
                        if (!isFormEditable && selectedTabIndex == 0) {
                            IconButton(onClick = { isFormEditable = true }) {
                                Icon(Icons.Default.Edit, contentDescription = "Editar", tint = primaryColor)
                            }
                        }
                        IconButton(onClick = { showDeleteDialog = true }) {
                            Icon(Icons.Default.Delete, contentDescription = "Eliminar", tint = errorColor)
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = surfaceColor)
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(backgroundColor)
        ) {
            if (!state.isNew) {
                TabRow(
                    selectedTabIndex = selectedTabIndex,
                    containerColor = surfaceColor,
                    contentColor = primaryColor
                ) {
                    tabs.forEachIndexed { index, title ->
                        Tab(
                            selected = selectedTabIndex == index,
                            onClick = { selectedTabIndex = index },
                            text = { Text(title, fontWeight = FontWeight.Bold) },
                            selectedContentColor = primaryColor,
                            unselectedContentColor = onSurfaceVariantColor
                        )
                    }
                }
            }

            if (selectedTabIndex == 0 || state.isNew) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    SectionHeader("DATOS GENERALES", primaryColor)

                    OutlinedTextField(
                        value = state.nombre,
                        onValueChange = { onEvent(PacienteFormUiEvent.NombreChanged(it)) },
                        label = { Text("Nombre *") },
                        isError = state.nombreError != null,
                        supportingText = { state.nombreError?.let { Text(it, color = errorColor) } },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        readOnly = !isFormEditable,
                        enabled = isFormEditable,
                        colors = inputColors
                    )

                    OutlinedTextField(
                        value = state.apellido,
                        onValueChange = { onEvent(PacienteFormUiEvent.ApellidoChanged(it)) },
                        label = { Text("Apellido *") },
                        isError = state.apellidoError != null,
                        supportingText = { state.apellidoError?.let { Text(it, color = errorColor) } },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        readOnly = !isFormEditable,
                        enabled = isFormEditable,
                        colors = inputColors
                    )

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(
                            value = state.edad,
                            onValueChange = { onEvent(PacienteFormUiEvent.EdadChanged(it)) },
                            label = { Text("Edad *") },
                            isError = state.edadError != null,
                            modifier = Modifier.weight(1f),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            singleLine = true,
                            readOnly = !isFormEditable,
                            enabled = isFormEditable,
                            colors = inputColors
                        )
                        OutlinedTextField(
                            value = state.sexo,
                            onValueChange = { onEvent(PacienteFormUiEvent.SexoChanged(it)) },
                            label = { Text("Sexo *") },
                            isError = state.sexoError != null,
                            modifier = Modifier.weight(1f),
                            singleLine = true,
                            readOnly = !isFormEditable,
                            enabled = isFormEditable,
                            colors = inputColors
                        )
                    }

                    SectionHeader(if (state.isNew) "DATOS DE LA CONSULTA" else "NUEVA CONSULTA / ACTUAL", primaryColor)

                    OutlinedTextField(
                        value = state.motivoConsulta,
                        onValueChange = { onEvent(PacienteFormUiEvent.MotivoConsultaChanged(it)) },
                        label = { Text("Motivo de Consulta / Diagnóstico *") },
                        isError = state.motivoConsultaError != null,
                        modifier = Modifier.fillMaxWidth(),
                        readOnly = !isFormEditable,
                        enabled = isFormEditable,
                        colors = inputColors
                    )

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(
                            value = state.presionArterial,
                            onValueChange = { onEvent(PacienteFormUiEvent.PresionArterialChanged(it)) },
                            label = { Text("TA (Ej. 120/80)") },
                            modifier = Modifier.weight(1f),
                            singleLine = true,
                            readOnly = !isFormEditable,
                            enabled = isFormEditable,
                            colors = inputColors
                        )

                        OutlinedTextField(
                            value = state.fc,
                            onValueChange = { newValue ->
                                if (newValue.all { it.isDigit() }) {
                                    onEvent(PacienteFormUiEvent.FcChanged(newValue))
                                }
                            },
                            label = { Text("FC") },
                            modifier = Modifier.weight(1f),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            singleLine = true,
                            readOnly = !isFormEditable,
                            enabled = isFormEditable,
                            colors = inputColors
                        )
                    }

                    OutlinedTextField(
                        value = state.antecedentesPatologicos,
                        onValueChange = { onEvent(PacienteFormUiEvent.AntecedentesPatologicosChanged(it)) },
                        label = { Text("Antecedentes Patológicos") },
                        modifier = Modifier.fillMaxWidth(),
                        readOnly = !isFormEditable,
                        enabled = isFormEditable,
                        colors = inputColors
                    )

                    OutlinedTextField(
                        value = state.tratamiento,
                        onValueChange = { onEvent(PacienteFormUiEvent.TratamientoChanged(it)) },
                        label = { Text("Tratamiento") },
                        modifier = Modifier.fillMaxWidth(),
                        readOnly = !isFormEditable,
                        enabled = isFormEditable,
                        colors = inputColors
                    )

                    OutlinedTextField(
                        value = state.alergias,
                        onValueChange = { onEvent(PacienteFormUiEvent.AlergiasChanged(it)) },
                        label = { Text("Alergias") },
                        modifier = Modifier.fillMaxWidth(),
                        readOnly = !isFormEditable,
                        enabled = isFormEditable,
                        colors = inputColors
                    )

                    OutlinedTextField(
                        value = state.plan,
                        onValueChange = { onEvent(PacienteFormUiEvent.PlanChanged(it)) },
                        label = { Text("Plan / Indicaciones") },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 3,
                        readOnly = !isFormEditable,
                        enabled = isFormEditable,
                        colors = inputColors
                    )

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable(enabled = isFormEditable) { showDatePicker = true }
                    ) {
                        OutlinedTextField(
                            value = dateString,
                            onValueChange = {},
                            label = { Text("Fecha de Cita *") },
                            isError = state.fechaCitaError != null,
                            modifier = Modifier.fillMaxWidth(),
                            enabled = false,
                            leadingIcon = {
                                Icon(Icons.Default.DateRange, contentDescription = null, tint = onSurfaceVariantColor)
                            },
                            colors = inputColors
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    if (isFormEditable) {
                        Button(
                            onClick = { onEvent(PacienteFormUiEvent.Save) },
                            enabled = !state.isSaving,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = primaryColor),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            if (state.isSaving) {
                                CircularProgressIndicator(color = onPrimaryColor, modifier = Modifier.size(24.dp))
                            } else {
                                Text(
                                    text = if (!state.isNew) "Guardar Actualización" else "Guardar Paciente",
                                    color = onPrimaryColor,
                                    fontSize = 16.sp
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))
                }
            }
            else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    contentPadding = PaddingValues(top = 16.dp, bottom = 20.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    if (state.historialVisitas.isEmpty()) {
                        item {
                            Text(
                                text = "No hay visitas registradas.",
                                color = onSurfaceVariantColor,
                                modifier = Modifier.padding(top = 16.dp)
                            )
                        }
                    } else {
                        items(state.historialVisitas) { visita ->
                            VisitaHistoryVerticalCard(visita = visita, formatter = dateFormatter)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun VisitaHistoryVerticalCard(visita: Visita, formatter: SimpleDateFormat) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.DateRange, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = visita.fechaCita?.let { formatter.format(Date(it)) } ?: "Sin fecha",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Motivo: ${visita.motivoConsulta}",
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )
            if (visita.diagnostico.isNotBlank()) {
                Text(
                    text = "Diagnóstico: ${visita.diagnostico}",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            HorizontalDivider(thickness = 1.dp, color = MaterialTheme.colorScheme.surfaceVariant)
            Spacer(modifier = Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "TA: ${visita.presionArterial.ifEmpty { "--" }}",
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "FC: ${visita.fc.ifEmpty { "--" }}",
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "Gluc: ${visita.glicemia.ifEmpty { "--" }}",
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}

@Composable
private fun SectionHeader(title: String, color: Color) {
    Text(
        text = title,
        fontSize = 13.sp,
        fontWeight = FontWeight.Bold,
        color = color,
        modifier = Modifier.padding(top = 10.dp, bottom = 2.dp)
    )
}