package com.mucheng.mucute.client.ui.theme

import android.content.Context
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext

@Composable
fun MuCuteClientTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val context = LocalContext.current
    val prefs = remember { context.getSharedPreferences("theme_prefs", Context.MODE_PRIVATE) }

    var primaryColor by remember { mutableStateOf(Color(prefs.getInt("primaryColor", Color(0xFF6200EE).toArgb()))) }
    var backgroundColor by remember { mutableStateOf(Color(prefs.getInt("backgroundColor", Color(0xFF121212).toArgb()))) }
    var surfaceColor by remember { mutableStateOf(Color(prefs.getInt("surfaceColor", Color(0xFF1E1E1E).toArgb()))) }

    val colorScheme = darkColorScheme(
        primary = primaryColor,
        background = backgroundColor,
        surface = surfaceColor
    )

    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}

