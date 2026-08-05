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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PacienteEditScreen(
    viewModel: PacienteAddViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit = {}
) {
    val context = LocalContext.current
    val tealColor = Color(0xFF006D77)

    var isFormEditable by remember(viewModel.isEditing) { mutableStateOf(!viewModel.isEditing) }
    var showDeleteDialog by remember { mutableStateOf(false) }

    val inputColors = OutlinedTextFieldDefaults.colors(
        focusedTextColor = Color.Black,
        unfocusedTextColor = Color.Black,
        focusedLabelColor = tealColor,
        unfocusedLabelColor = Color.DarkGray,
        focusedBorderColor = tealColor,
        unfocusedBorderColor = Color.Gray,
        cursorColor = tealColor,
        disabledTextColor = Color.Black,
        disabledBorderColor = Color.Gray,
        disabledLabelColor = Color.DarkGray,
        disabledLeadingIconColor = Color.Black
    )

    var showDatePicker by remember { mutableStateOf(false) }

    val dateFormatter = remember {
        SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).apply {
            timeZone = java.util.TimeZone.getTimeZone("UTC")
        }
    }
    val dateString = viewModel.fechaCita?.let { dateFormatter.format(Date(it)) } ?: "Seleccionar fecha"

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
                        viewModel.deletePaciente(onSuccess = {
                            Toast.makeText(context, "Paciente eliminado correctamente", Toast.LENGTH_SHORT).show()
                            onNavigateBack()
                        })
                    }
                ) {
                    Text(
                        text = "Sí",
                        color = Color.Red,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text(
                        text = "No",
                        color = Color.DarkGray,
                        fontWeight = FontWeight.Medium,
                        fontSize = 16.sp
                    )
                }
            },
            containerColor = Color.White,
            titleContentColor = Color.Black,
            textContentColor = Color(0xFF333333)
        )
    }

    if (showDatePicker && isFormEditable) {
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = viewModel.fechaCita ?: System.currentTimeMillis()
        )

        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.fechaCita = datePickerState.selectedDateMillis
                    showDatePicker = false
                }) {
                    Text("Aceptar", color = tealColor)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("Cancelar", color = Color.Gray)
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (viewModel.isEditing) "Ficha Médica del Paciente" else "Registrar Paciente",
                        color = Color.Black,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver",
                            tint = Color.Black
                        )
                    }
                },
                actions = {
                    if (viewModel.isEditing && !isFormEditable) {
                        IconButton(onClick = { isFormEditable = true }) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Editar",
                                tint = Color(0xFF5D9CFF)
                            )
                        }
                    }

                    if (viewModel.isEditing) {
                        IconButton(onClick = { showDeleteDialog = true }) {
                            Icon(Icons.Default.Delete, contentDescription = "Eliminar", tint = Color.Red)
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color.White)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            SectionHeader("DATOS GENERALES")

            OutlinedTextField(
                value = viewModel.nombre,
                onValueChange = { viewModel.nombre = it },
                label = { Text("Nombre Completo *") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                readOnly = !isFormEditable,
                enabled = isFormEditable,
                colors = inputColors,
                textStyle = TextStyle(color = Color.Black, fontSize = 16.sp)
            )

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = viewModel.edad,
                    onValueChange = { viewModel.edad = it },
                    label = { Text("Edad *") },
                    modifier = Modifier.weight(1f),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    readOnly = !isFormEditable,
                    enabled = isFormEditable,
                    colors = inputColors,
                    textStyle = TextStyle(color = Color.Black, fontSize = 16.sp)
                )
                OutlinedTextField(
                    value = viewModel.sexo,
                    onValueChange = { viewModel.sexo = it },
                    label = { Text("Sexo *") },
                    modifier = Modifier.weight(1f),
                    singleLine = true,
                    readOnly = !isFormEditable,
                    enabled = isFormEditable,
                    colors = inputColors,
                    textStyle = TextStyle(color = Color.Black, fontSize = 16.sp)
                )
            }

            OutlinedTextField(
                value = viewModel.motivoConsulta,
                onValueChange = { viewModel.motivoConsulta = it },
                label = { Text("Motivo de Consulta / Diagnóstico *") },
                modifier = Modifier.fillMaxWidth(),
                readOnly = !isFormEditable,
                enabled = isFormEditable,
                colors = inputColors,
                textStyle = TextStyle(color = Color.Black, fontSize = 16.sp)
            )

            SectionHeader("SIGNOS VITALES")

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = viewModel.presionArterial,
                    onValueChange = { viewModel.presionArterial = it },
                    label = { Text("TA (Ej. 120/80)") },
                    modifier = Modifier.weight(1f),
                    singleLine = true,
                    readOnly = !isFormEditable,
                    enabled = isFormEditable,
                    colors = inputColors,
                    textStyle = TextStyle(color = Color.Black, fontSize = 16.sp)
                )
                OutlinedTextField(
                    value = viewModel.fc,
                    onValueChange = { viewModel.fc = it },
                    label = { Text("FC (Frec. Cardíaca)") },
                    modifier = Modifier.weight(1f),
                    singleLine = true,
                    readOnly = !isFormEditable,
                    enabled = isFormEditable,
                    colors = inputColors,
                    textStyle = TextStyle(color = Color.Black, fontSize = 16.sp)
                )
            }

            SectionHeader("ANTECEDENTES Y TRATAMIENTO")

            OutlinedTextField(
                value = viewModel.antecedentesPatologicos,
                onValueChange = { viewModel.antecedentesPatologicos = it },
                label = { Text("Antecedentes Patológicos") },
                modifier = Modifier.fillMaxWidth(),
                readOnly = !isFormEditable,
                enabled = isFormEditable,
                colors = inputColors,
                textStyle = TextStyle(color = Color.Black, fontSize = 16.sp)
            )

            OutlinedTextField(
                value = viewModel.tratamiento,
                onValueChange = { viewModel.tratamiento = it },
                label = { Text("Tratamiento Actual") },
                modifier = Modifier.fillMaxWidth(),
                readOnly = !isFormEditable,
                enabled = isFormEditable,
                colors = inputColors,
                textStyle = TextStyle(color = Color.Black, fontSize = 16.sp)
            )

            OutlinedTextField(
                value = viewModel.alergias,
                onValueChange = { viewModel.alergias = it },
                label = { Text("Alergias") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                readOnly = !isFormEditable,
                enabled = isFormEditable,
                colors = inputColors,
                textStyle = TextStyle(color = Color.Black, fontSize = 16.sp)
            )

            SectionHeader("PRUEBAS DE LABORATORIO")

            OutlinedTextField(
                value = viewModel.hb,
                onValueChange = { viewModel.hb = it },
                label = { Text("Resultados de laboratorio") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 2,
                readOnly = !isFormEditable,
                enabled = isFormEditable,
                colors = inputColors,
                textStyle = TextStyle(color = Color.Black, fontSize = 16.sp)
            )

            SectionHeader("IMÁGENES")

            OutlinedTextField(
                value = viewModel.fevi,
                onValueChange = { viewModel.fevi = it },
                label = { Text("Estudios de imágenes") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 2,
                readOnly = !isFormEditable,
                enabled = isFormEditable,
                colors = inputColors,
                textStyle = TextStyle(color = Color.Black, fontSize = 16.sp)
            )

            SectionHeader("PLAN Y TRATAMIENTO RECOMENDADO")

            OutlinedTextField(
                value = viewModel.plan,
                onValueChange = { viewModel.plan = it },
                label = { Text("Plan / Indicaciones") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3,
                readOnly = !isFormEditable,
                enabled = isFormEditable,
                colors = inputColors,
                textStyle = TextStyle(color = Color.Black, fontSize = 16.sp)
            )

            SectionHeader("PROGRAMACIÓN DE CITA")

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(enabled = isFormEditable) { showDatePicker = true }
            ) {
                OutlinedTextField(
                    value = dateString,
                    onValueChange = {},
                    label = { Text("Fecha de Cita *") },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = false,
                    leadingIcon = {
                        Icon(Icons.Default.DateRange, contentDescription = "Icono de calendario")
                    },
                    colors = inputColors,
                    textStyle = TextStyle(color = Color.Black, fontSize = 16.sp)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            if (isFormEditable) {
                Button(
                    onClick = {
                        viewModel.savePaciente(
                            onSuccess = {
                                Toast.makeText(
                                    context,
                                    if (viewModel.isEditing) "Paciente actualizado" else "Paciente guardado",
                                    Toast.LENGTH_SHORT
                                ).show()
                                onNavigateBack()
                            },
                            onError = { errorMsg ->
                                Toast.makeText(context, errorMsg, Toast.LENGTH_SHORT).show()
                            }
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = tealColor),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = if (viewModel.isEditing) "Actualizar Paciente" else "Guardar Paciente",
                        color = Color.White,
                        fontSize = 16.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
private fun SectionHeader(title: String) {
    Text(
        text = title,
        fontSize = 13.sp,
        fontWeight = FontWeight.Bold,
        color = Color(0xFF006D77),
        modifier = Modifier.padding(top = 10.dp, bottom = 2.dp)
    )
}