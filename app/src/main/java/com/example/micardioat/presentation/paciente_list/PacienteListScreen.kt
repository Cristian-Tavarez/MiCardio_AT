package com.example.micardioat.presentation.paciente_list

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.micardioat.domain.model.PacienteDetalle
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun PacienteListScreen(
    viewModel: PacienteListViewModel = hiltViewModel(),
    onNavigateToDetail: (Int?) -> Unit = {}
) {
    val pacientes by viewModel.pacientes.collectAsStateWithLifecycle()

    PacienteListBody(
        pacientes = pacientes,
        onNavigateToDetail = onNavigateToDetail,
        onQuickSchedule = viewModel::quickScheduleVisit
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PacienteListBody(
    pacientes: List<PacienteDetalle>,
    onNavigateToDetail: (Int?) -> Unit,
    onQuickSchedule: (PacienteDetalle, Long) -> Unit
) {
    val currentLocale = Locale.forLanguageTag("es-DO")

    val lightBackground = MaterialTheme.colorScheme.background
    val accentTeal = MaterialTheme.colorScheme.primary
    val cardBackground = MaterialTheme.colorScheme.surfaceVariant
    val textPrimary = MaterialTheme.colorScheme.onBackground
    val textSecondary = MaterialTheme.colorScheme.onSurfaceVariant
    val onAccentText = MaterialTheme.colorScheme.onPrimary

    var selectedDateMillis by remember { mutableStateOf(System.currentTimeMillis()) }
    var isSearchActive by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }
    var showScheduleDialog by remember { mutableStateOf(false) }

    val filteredPacientes = pacientes.filter { detalle ->
        if (isSearchActive && searchQuery.isNotBlank()) {
            detalle.paciente.nombre.contains(searchQuery, ignoreCase = true)
        } else {
            detalle.visitas.any { visita ->
                visita.fechaCita != null && isSameDay(visita.fechaCita, selectedDateMillis)
            }
        }
    }

    val cardDateFormatter = remember(currentLocale) {
        SimpleDateFormat("dd\nMMM", currentLocale).apply {
            timeZone = java.util.TimeZone.getTimeZone("UTC")
        }
    }

    if (showScheduleDialog) {
        QuickScheduleDialog(
            pacientes = pacientes,
            selectedDateMillis = selectedDateMillis,
            accentTeal = accentTeal,
            onDismiss = { showScheduleDialog = false },
            onConfirm = { detalle ->
                onQuickSchedule(detalle, selectedDateMillis)
                showScheduleDialog = false
            }
        )
    }

    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(lightBackground)
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            if (isSearchActive) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Buscar paciente por nombre...", color = textSecondary) },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = accentTeal,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant,
                        focusedContainerColor = MaterialTheme.colorScheme.surface,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                        focusedTextColor = textPrimary,
                        unfocusedTextColor = textPrimary
                    ),
                    leadingIcon = {
                        Icon(Icons.Default.Search, contentDescription = "Buscar", tint = accentTeal)
                    },
                    trailingIcon = {
                        IconButton(onClick = {
                            if (searchQuery.isNotEmpty()) {
                                searchQuery = ""
                            } else {
                                isSearchActive = false
                            }
                        }) {
                            Icon(Icons.Default.Close, contentDescription = "Cerrar búsqueda", tint = textSecondary)
                        }
                    }
                )
            } else {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Inicio",
                        color = textPrimary,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Row {
                        IconButton(onClick = { isSearchActive = true }) {
                            Icon(Icons.Default.Search, contentDescription = "Buscar", tint = textPrimary)
                        }
                        IconButton(onClick = { showScheduleDialog = true }) {
                            Icon(Icons.Default.DateRange, contentDescription = "Programar", tint = accentTeal)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = SimpleDateFormat("MMMM yyyy", currentLocale).format(Date(selectedDateMillis)).uppercase(),
                color = accentTeal,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            HorizontalCalendar(
                selectedDateMillis = selectedDateMillis,
                onDateSelected = { newDate ->
                    selectedDateMillis = newDate
                    isSearchActive = false
                    searchQuery = ""
                },
                accentColor = accentTeal,
                onAccentTextColor = onAccentText,
                unselectedBgColor = cardBackground,
                primaryTextColor = textPrimary,
                secondaryTextColor = textSecondary
            )

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (isSearchActive && searchQuery.isNotBlank()) "Resultados de búsqueda" else "Citas para este día",
                    color = textPrimary,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(bottom = 16.dp)
            ) {
                if (filteredPacientes.isEmpty()) {
                    item {
                        Text(
                            text = if (isSearchActive && searchQuery.isNotBlank())
                                "No se encontraron pacientes."
                            else
                                "No hay citas programadas para este día.",
                            color = textSecondary,
                            modifier = Modifier.padding(top = 16.dp)
                        )
                    }
                } else {
                    items(filteredPacientes) { detalle ->
                        val paciente = detalle.paciente

                        val relevantVisit = if (isSearchActive && searchQuery.isNotBlank()) {
                            detalle.visitas.maxByOrNull { it.fechaCita ?: 0L }
                        } else {
                            detalle.visitas.find { it.fechaCita != null && isSameDay(it.fechaCita, selectedDateMillis) }
                        }

                        val displayDate = relevantVisit?.fechaCita?.let {
                            cardDateFormatter.format(Date(it)).uppercase()
                        } ?: "SIN\nFECHA"

                        val diagnosticoStr = relevantVisit?.diagnostico ?: ""
                        val motivoStr = relevantVisit?.motivoConsulta ?: ""
                        val detailsText = diagnosticoStr.ifBlank { motivoStr }

                        AppointmentCard(
                            date = displayDate,
                            accentColor = accentTeal,
                            onAccentTextColor = onAccentText,
                            cardBgColor = cardBackground,
                            textColor = textPrimary,
                            subTextColor = textSecondary,
                            name = "${paciente.nombre} ${paciente.apellido}".trim(),
                            details = detailsText.ifBlank { "Sin especificar" },
                            modifier = Modifier.clickable {
                                onNavigateToDetail(paciente.pacienteId)
                            }
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuickScheduleDialog(
    pacientes: List<PacienteDetalle>,
    selectedDateMillis: Long,
    accentTeal: Color,
    onDismiss: () -> Unit,
    onConfirm: (PacienteDetalle) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedDetalle by remember { mutableStateOf<PacienteDetalle?>(null) }
    val displayDate = SimpleDateFormat("dd/MM/yyyy",
        Locale.forLanguageTag("es-DO")).format(Date(selectedDateMillis))

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Programar Cita Existente", fontWeight = FontWeight.Bold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                Text("Fecha seleccionada: $displayDate")

                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded }
                ) {
                    OutlinedTextField(
                        value = selectedDetalle?.paciente?.nombre ?: "",
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Seleccionar Paciente") },
                        placeholder = { Text("Toca para elegir...") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = accentTeal,
                            focusedLabelColor = accentTeal
                        ),
                        modifier = Modifier
                            .menuAnchor(MenuAnchorType.PrimaryNotEditable, true)
                            .fillMaxWidth()
                    )

                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        if (pacientes.isEmpty()) {
                            DropdownMenuItem(
                                text = { Text("No hay pacientes registrados") },
                                onClick = { expanded = false }
                            )
                        } else {
                            pacientes.sortedBy { it.paciente.nombre }.forEach { detalle ->
                                DropdownMenuItem(
                                    text = { Text(detalle.paciente.nombre) },
                                    onClick = {
                                        selectedDetalle = detalle
                                        expanded = false
                                    }
                                )
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = { selectedDetalle?.let { onConfirm(it) } },
                enabled = selectedDetalle != null,
                colors = ButtonDefaults.buttonColors(containerColor = accentTeal)
            ) {
                Text("Confirmar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar", color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    )
}

@Composable
fun HorizontalCalendar(
    selectedDateMillis: Long,
    onDateSelected: (Long) -> Unit,
    accentColor: Color,
    onAccentTextColor: Color,
    unselectedBgColor: Color,
    primaryTextColor: Color,
    secondaryTextColor: Color
) {
    val currentLocale = Locale.forLanguageTag("es-DO")

    val days = remember {
        val calendar = Calendar.getInstance()
        val list = mutableListOf<Long>()
        repeat(31) {
            list.add(calendar.timeInMillis)
            calendar.add(Calendar.DAY_OF_YEAR, 1)
        }
        list
    }

    val dayNameFormatter = remember(currentLocale) { SimpleDateFormat("EEE", currentLocale) }
    val dayNumberFormatter = remember(currentLocale) { SimpleDateFormat("dd", currentLocale) }
    val exactDateFormatter = remember(currentLocale) { SimpleDateFormat("yyyy-MM-dd", currentLocale) }

    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        items(days) { dayMillis ->

            val isSelected = exactDateFormatter.format(Date(dayMillis)) == exactDateFormatter.format(Date(selectedDateMillis))

            val backgroundColor = if (isSelected) accentColor else unselectedBgColor
            val topTextColor = if (isSelected) onAccentTextColor else secondaryTextColor
            val bottomTextColor = if (isSelected) onAccentTextColor else primaryTextColor

            Column(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(backgroundColor)
                    .clickable { onDateSelected(dayMillis) }
                    .padding(vertical = 16.dp, horizontal = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = dayNameFormatter.format(Date(dayMillis)).uppercase(),
                    color = topTextColor,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = dayNumberFormatter.format(Date(dayMillis)),
                    color = bottomTextColor,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun AppointmentCard(
    date: String,
    accentColor: Color,
    onAccentTextColor: Color,
    cardBgColor: Color,
    textColor: Color,
    subTextColor: Color,
    name: String,
    details: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(cardBgColor, RoundedCornerShape(12.dp))
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(64.dp)
                .background(accentColor, RoundedCornerShape(12.dp)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = date,
                color = onAccentTextColor,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column {
            Text(
                text = name,
                color = textColor,
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = details,
                color = subTextColor,
                fontSize = 12.sp
            )
        }
    }
}

fun isSameDay(utcMillis: Long, localMillis: Long): Boolean {
    val currentLocale = Locale.forLanguageTag("es-DO")

    val utcFormatter = SimpleDateFormat("yyyy-MM-dd", currentLocale).apply {
        timeZone = java.util.TimeZone.getTimeZone("UTC")
    }
    val dateFromDb = utcFormatter.format(Date(utcMillis))

    val localFormatter = SimpleDateFormat("yyyy-MM-dd", currentLocale)
    val dateFromCalendar = localFormatter.format(Date(localMillis))

    return dateFromDb == dateFromCalendar
}