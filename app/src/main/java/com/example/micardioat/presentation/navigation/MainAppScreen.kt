package com.example.micardioat.presentation.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun MainAppScreen(
    navController: NavController,
    content: @Composable (Modifier) -> Unit
) {
    var selectedItem by remember { mutableStateOf(0) }
    val items = listOf("Inicio", "Pacientes", "Ajustes")
    val icons = listOf(Icons.Default.DateRange, Icons.Default.People, Icons.Default.Settings)

    val accentTeal = Color(0xFF006D77)
    val indicatorColor = Color(0xFFB2EBF2)

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination?.route

    val showBottomBar = currentDestination?.contains("PacienteList") == true ||
            currentDestination?.contains("Patients") == true ||
            currentDestination?.contains("Settings") == true

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                NavigationBar(
                    containerColor = Color.White,
                    contentColor = Color.Gray
                ) {
                    items.forEachIndexed { index, item ->
                        NavigationBarItem(
                            icon = { Icon(icons[index], contentDescription = item) },
                            label = { Text(item) },
                            selected = selectedItem == index,
                            onClick = {
                                selectedItem = index
                                val route = when(index) {
                                    0 -> Screen.PacienteList
                                    1 -> Screen.Patients
                                    else -> Screen.Settings
                                }
                                navController.navigate(route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = accentTeal,
                                selectedTextColor = accentTeal,
                                indicatorColor = indicatorColor,
                                unselectedIconColor = Color.Gray,
                                unselectedTextColor = Color.Gray
                            )
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        content(Modifier.padding(innerPadding))
    }
}