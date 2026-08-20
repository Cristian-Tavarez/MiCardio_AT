package com.example.micardioat.presentation.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier

@Composable
fun MainAppScreen(
    backStack: SnapshotStateList<Screen>,
    content: @Composable (Modifier) -> Unit
) {
    val items = listOf("Inicio", "Pacientes", "Ajustes")

    val icons = listOf(
        Icons.Default.DateRange,
        Icons.Default.People,
        Icons.Default.Settings
    )

    val currentDestination = backStack.lastOrNull()

    val selectedItem = when (currentDestination) {
        Screen.PacienteList -> 0
        Screen.Patients -> 1
        Screen.Settings -> 2
        else -> 0
    }

    val showBottomBar =
        currentDestination == Screen.PacienteList ||
                currentDestination == Screen.Patients ||
                currentDestination == Screen.Settings

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                NavigationBar(
                    containerColor = MaterialTheme.colorScheme.surface,
                    contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                ) {
                    items.forEachIndexed { index, item ->
                        NavigationBarItem(
                            icon = {
                                Icon(
                                    imageVector = icons[index],
                                    contentDescription = item
                                )
                            },
                            label = {
                                Text(item)
                            },
                            selected = selectedItem == index,
                            onClick = {
                                val destination = when (index) {
                                    0 -> Screen.PacienteList
                                    1 -> Screen.Patients
                                    else -> Screen.Settings
                                }

                                if (currentDestination != destination) {
                                    backStack.clear()
                                    backStack.add(destination)
                                }
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor =
                                    MaterialTheme.colorScheme.primary,
                                selectedTextColor =
                                    MaterialTheme.colorScheme.primary,
                                indicatorColor =
                                    MaterialTheme.colorScheme.primaryContainer,
                                unselectedIconColor =
                                    MaterialTheme.colorScheme.onSurfaceVariant,
                                unselectedTextColor =
                                    MaterialTheme.colorScheme.onSurfaceVariant
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