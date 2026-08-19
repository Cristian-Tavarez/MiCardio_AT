package com.example.micardioat

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import com.example.micardioat.presentation.navigation.AppNavigation
import com.example.micardioat.ui.theme.MiCardioATTheme
import com.example.micardioat.utils.AppThemeMode
import com.example.micardioat.utils.ThemePreferences
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var themePreferences: ThemePreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val currentTheme by themePreferences.themeMode.collectAsState(initial = AppThemeMode.SYSTEM)
            val coroutineScope = rememberCoroutineScope()

            MiCardioATTheme(themeMode = currentTheme) {
                AppNavigation(
                    currentTheme = currentTheme,
                    onThemeChange = { newTheme ->
                        coroutineScope.launch {
                            themePreferences.saveThemeMode(newTheme)
                        }
                    }
                )
            }
        }
    }
}