package com.example.micardioat.presentation.paciente_list

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.micardioat.domain.model.PacienteCardiologia
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date

@Composable
fun PacienteListScreen(
    viewModel: PacienteListViewModel = hiltViewModel(),
    onNavigateToDetail: (Int?) -> Unit = {},
    onLogout: () -> Unit = {}
) {
    val pacientes by viewModel.pacientes.collectAsState()
    val currentLocale = LocalConfiguration.current.locales[0]

    val lightBackground = Color.White
    val accentTeal = Color(0xFF006D77)
    val cardBackground = Color(0xFFF5F5F5)
    val textPrimary = Color.Black
    val textSecondary = Color.DarkGray

    var selectedDateMillis by remember { mutableStateOf(System.currentTimeMillis()) }
    var isSearchActive by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }

    val filteredPacientes = pacientes.filter { paciente ->
        if (isSearchActive && searchQuery.isNotBlank()) {
            paciente.nombre.contains(searchQuery, ignoreCase = true)
        } else {
            paciente.fechaCita != null && isSameDay(paciente.fechaCita, selectedDateMillis)
        }
    }

    val cardDateFormatter = remember(currentLocale) {
        SimpleDateFormat("dd\nMMM", currentLocale).apply {
            timeZone = java.util.TimeZone.getTimeZone("UTC")
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(lightBackground)
            .padding(16.dp)
    ) {
        if (isSearchActive) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Buscar paciente por nombre...") },
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = accentTeal,
                    unfocusedBorderColor = Color.LightGray,
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White
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
                    text = SimpleDateFormat("MMMM yyyy", currentLocale).format(Date()).uppercase(),
                    color = accentTeal,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { isSearchActive = true }) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Buscar",
                            tint = textPrimary
                        )
                    }
                    IconButton(onClick = onLogout) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                            contentDescription = "Cerrar Sesión",
                            tint = Color(0xFFFF5252)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        HorizontalCalendar(
            selectedDateMillis = selectedDateMillis,
            onDateSelected = { newDate ->
                selectedDateMillis = newDate
                isSearchActive = false
                searchQuery = ""
            },
            accentColor = accentTeal,
            unselectedBgColor = cardBackground,
            primaryTextColor = textPrimary,
            secondaryTextColor = textSecondary
        )

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = if (isSearchActive && searchQuery.isNotBlank()) "Resultados de búsqueda" else "Mis Citas",
                color = textPrimary,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = "+ Añadir",
                color = accentTeal,
                fontSize = 16.sp,
                modifier = Modifier.clickable {
                    onNavigateToDetail(null)
                }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (filteredPacientes.isEmpty()) {
                item {
                    Text(
                        text = if (isSearchActive && searchQuery.isNotBlank())
                            "No se encontraron pacientes."
                        else
                            "No hay citas programadas para este día.",
                        color = Color.Gray,
                        modifier = Modifier.padding(top = 16.dp)
                    )
                }
            } else {
                items(filteredPacientes) { paciente ->
                    val displayDate = paciente.fechaCita?.let {
                        cardDateFormatter.format(Date(it)).uppercase()
                    } ?: "SIN\nFECHA"

                    AppointmentCard(
                        date = displayDate,
                        accentColor = accentTeal,
                        cardBgColor = cardBackground,
                        textColor = textPrimary,
                        subTextColor = textSecondary,
                        name = paciente.nombre,
                        details = "Diagnóstico: ${if (paciente.diagnostico.isNotBlank()) paciente.diagnostico else paciente.motivoConsulta}",
                        modifier = Modifier.clickable {
                            onNavigateToDetail(paciente.pacienteId)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun HorizontalCalendar(
    selectedDateMillis: Long,
    onDateSelected: (Long) -> Unit,
    accentColor: Color,
    unselectedBgColor: Color,
    primaryTextColor: Color,
    secondaryTextColor: Color
) {
    val currentLocale = LocalConfiguration.current.locales[0]

    val days = remember {
        val calendar = Calendar.getInstance()
        val list = mutableListOf<Long>()
        for (i in 0..30) {
            list.add(calendar.timeInMillis)
            calendar.add(Calendar.DAY_OF_YEAR, 1)
        }
        list
    }

    val dayNameFormatter = remember(currentLocale) { SimpleDateFormat("EEE", currentLocale) }
    val dayNumberFormatter = remember(currentLocale) { SimpleDateFormat("dd", currentLocale) }

    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        items(days) { dayMillis ->
            val isSelected = isSameDay(dayMillis, selectedDateMillis)
            val backgroundColor = if (isSelected) accentColor else unselectedBgColor
            val topTextColor = if (isSelected) Color.White else secondaryTextColor
            val bottomTextColor = if (isSelected) Color.White else primaryTextColor

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
                color = Color.White,
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
    val utcFormatter = SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault()).apply {
        timeZone = java.util.TimeZone.getTimeZone("UTC")
    }
    val dateFromDb = utcFormatter.format(Date(utcMillis))

    val localFormatter = SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault())
    val dateFromCalendar = localFormatter.format(Date(localMillis))

    return dateFromDb == dateFromCalendar
}