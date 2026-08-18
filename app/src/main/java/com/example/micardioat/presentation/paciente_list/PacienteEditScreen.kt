package com.example.micardioat.presentation.paciente_list

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
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

    // Connected to your dynamic theme colors
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

    LaunchedEffect(state.saved, state.deleted) {
        if (state.saved) {
            Toast.makeText(
                context,
                if (!state.isNew) "Paciente actualizado" else "Paciente guardado",
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
                Text(
                    text = "Eliminar Paciente",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            },
            text = {
                Text(
                    text = "¿Estás seguro de que deseas eliminar este paciente? Esta acción no se puede deshacer.",
                    fontSize = 15.sp
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showDeleteDialog = false
                        onEvent(PacienteFormUiEvent.Delete)
                    }
                ) {
                    Text(
                        text = "Sí",
                        color = errorColor,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text(
                        text = "No",
                        color = onSurfaceVariantColor,
                        fontWeight = FontWeight.Medium,
                        fontSize = 16.sp
                    )
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
                        text = if (!state.isNew) "Ficha Médica del Paciente" else "Registrar Paciente",
                        color = onSurfaceColor,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver",
                            tint = onSurfaceColor
                        )
                    }
                },
                actions = {
                    if (!state.isNew && !isFormEditable) {
                        IconButton(onClick = { isFormEditable = true }) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Editar",
                                tint = primaryColor
                            )
                        }
                    }

                    if (!state.isNew) {
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
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            SectionHeader("DATOS GENERALES", primaryColor)

            OutlinedTextField(
                value = state.nombre,
                onValueChange = { onEvent(PacienteFormUiEvent.NombreChanged(it)) },
                label = { Text("Nombre Completo *") },
                isError = state.nombreError != null,
                supportingText = { state.nombreError?.let { Text(it, color = errorColor) } },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                readOnly = !isFormEditable,
                enabled = isFormEditable,
                colors = inputColors,
                textStyle = TextStyle(color = onSurfaceColor, fontSize = 16.sp)
            )

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = state.edad,
                    onValueChange = { onEvent(PacienteFormUiEvent.EdadChanged(it)) },
                    label = { Text("Edad *") },
                    isError = state.edadError != null,
                    supportingText = { state.edadError?.let { Text(it, color = errorColor) } },
                    modifier = Modifier.weight(1f),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    readOnly = !isFormEditable,
                    enabled = isFormEditable,
                    colors = inputColors,
                    textStyle = TextStyle(color = onSurfaceColor, fontSize = 16.sp)
                )
                OutlinedTextField(
                    value = state.sexo,
                    onValueChange = { onEvent(PacienteFormUiEvent.SexoChanged(it)) },
                    label = { Text("Sexo *") },
                    isError = state.sexoError != null,
                    supportingText = { state.sexoError?.let { Text(it, color = errorColor) } },
                    modifier = Modifier.weight(1f),
                    singleLine = true,
                    readOnly = !isFormEditable,
                    enabled = isFormEditable,
                    colors = inputColors,
                    textStyle = TextStyle(color = onSurfaceColor, fontSize = 16.sp)
                )
            }

            OutlinedTextField(
                value = state.motivoConsulta,
                onValueChange = { onEvent(PacienteFormUiEvent.MotivoConsultaChanged(it)) },
                label = { Text("Motivo de Consulta / Diagnóstico *") },
                isError = state.motivoConsultaError != null,
                supportingText = { state.motivoConsultaError?.let { Text(it, color = errorColor) } },
                modifier = Modifier.fillMaxWidth(),
                readOnly = !isFormEditable,
                enabled = isFormEditable,
                colors = inputColors,
                textStyle = TextStyle(color = onSurfaceColor, fontSize = 16.sp)
            )

            SectionHeader("SIGNOS VITALES", primaryColor)

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = state.presionArterial,
                    onValueChange = { onEvent(PacienteFormUiEvent.PresionArterialChanged(it)) },
                    label = { Text("TA (Ej. 120/80)") },
                    modifier = Modifier.weight(1f),
                    singleLine = true,
                    readOnly = !isFormEditable,
                    enabled = isFormEditable,
                    colors = inputColors,
                    textStyle = TextStyle(color = onSurfaceColor, fontSize = 16.sp)
                )
                OutlinedTextField(
                    value = state.fc,
                    onValueChange = { onEvent(PacienteFormUiEvent.FcChanged(it)) },
                    label = { Text("FC (Frec. Cardíaca)") },
                    modifier = Modifier.weight(1f),
                    singleLine = true,
                    readOnly = !isFormEditable,
                    enabled = isFormEditable,
                    colors = inputColors,
                    textStyle = TextStyle(color = onSurfaceColor, fontSize = 16.sp)
                )
            }

            SectionHeader("ANTECEDENTES Y TRATAMIENTO", primaryColor)

            OutlinedTextField(
                value = state.antecedentesPatologicos,
                onValueChange = { onEvent(PacienteFormUiEvent.AntecedentesPatologicosChanged(it)) },
                label = { Text("Antecedentes Patológicos") },
                modifier = Modifier.fillMaxWidth(),
                readOnly = !isFormEditable,
                enabled = isFormEditable,
                colors = inputColors,
                textStyle = TextStyle(color = onSurfaceColor, fontSize = 16.sp)
            )

            OutlinedTextField(
                value = state.tratamiento,
                onValueChange = { onEvent(PacienteFormUiEvent.TratamientoChanged(it)) },
                label = { Text("Tratamiento Actual") },
                modifier = Modifier.fillMaxWidth(),
                readOnly = !isFormEditable,
                enabled = isFormEditable,
                colors = inputColors,
                textStyle = TextStyle(color = onSurfaceColor, fontSize = 16.sp)
            )

            OutlinedTextField(
                value = state.alergias,
                onValueChange = { onEvent(PacienteFormUiEvent.AlergiasChanged(it)) },
                label = { Text("Alergias") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                readOnly = !isFormEditable,
                enabled = isFormEditable,
                colors = inputColors,
                textStyle = TextStyle(color = onSurfaceColor, fontSize = 16.sp)
            )

            SectionHeader("PRUEBAS DE LABORATORIO", primaryColor)

            OutlinedTextField(
                value = state.hb,
                onValueChange = { onEvent(PacienteFormUiEvent.HbChanged(it)) },
                label = { Text("Resultados de laboratorio") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 2,
                readOnly = !isFormEditable,
                enabled = isFormEditable,
                colors = inputColors,
                textStyle = TextStyle(color = onSurfaceColor, fontSize = 16.sp)
            )

            SectionHeader("IMÁGENES", primaryColor)

            OutlinedTextField(
                value = state.fevi,
                onValueChange = { onEvent(PacienteFormUiEvent.FeviChanged(it)) },
                label = { Text("Estudios de imágenes") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 2,
                readOnly = !isFormEditable,
                enabled = isFormEditable,
                colors = inputColors,
                textStyle = TextStyle(color = onSurfaceColor, fontSize = 16.sp)
            )

            SectionHeader("PLAN Y TRATAMIENTO RECOMENDADO", primaryColor)

            OutlinedTextField(
                value = state.plan,
                onValueChange = { onEvent(PacienteFormUiEvent.PlanChanged(it)) },
                label = { Text("Plan / Indicaciones") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3,
                readOnly = !isFormEditable,
                enabled = isFormEditable,
                colors = inputColors,
                textStyle = TextStyle(color = onSurfaceColor, fontSize = 16.sp)
            )

            SectionHeader("PROGRAMACIÓN DE CITA", primaryColor)

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
                    supportingText = { state.fechaCitaError?.let { Text(it, color = errorColor) } },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = false,
                    leadingIcon = {
                        Icon(Icons.Default.DateRange, contentDescription = "Icono de calendario", tint = onSurfaceVariantColor)
                    },
                    colors = inputColors,
                    textStyle = TextStyle(color = onSurfaceColor, fontSize = 16.sp)
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
                            text = if (!state.isNew) "Actualizar Paciente" else "Guardar Paciente",
                            color = onPrimaryColor,
                            fontSize = 16.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
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

@Preview(showBackground = true)
@Composable
private fun PacienteEditBodyPreview() {
    MaterialTheme {
        PacienteEditBody(
            state = PacienteFormUiState(
                nombre = "Juan Pérez",
                edad = "45",
                sexo = "Masculino",
                motivoConsulta = "Evaluación cardiaca de rutina",
                isNew = false
            ),
            onEvent = {},
            onNavigateBack = {}
        )
    }
}