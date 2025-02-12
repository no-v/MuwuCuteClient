package com.mucheng.mucute.client.router.main

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.madrapps.pikolo.HSLColorPicker
import com.madrapps.pikolo.listeners.OnColorSelectionListener
import com.mucheng.mucute.client.R
import com.mucheng.mucute.client.util.LocalSnackbarHostState
import com.mucheng.mucute.client.util.SnackbarHostStateScope

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsPageContent() {
    SnackbarHostStateScope {
        val snackbarHostState = LocalSnackbarHostState.current
        val context = LocalContext.current
        val prefs = remember { context.getSharedPreferences("theme_prefs", Context.MODE_PRIVATE) }
        var primaryColor by remember { mutableStateOf(getStoredColor(prefs, "primaryColor", Color(0xFF6200EE))) }
        var backgroundColor by remember { mutableStateOf(getStoredColor(prefs, "backgroundColor", Color(0xFF121212))) }
        var surfaceColor by remember { mutableStateOf(getStoredColor(prefs, "surfaceColor", Color(0xFF1E1E1E))) }

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(stringResource(R.string.settings)) },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.surfaceContainer,
                        titleContentColor = contentColorFor(MaterialTheme.colorScheme.surfaceContainer)
                    )
                )
            },
            bottomBar = {
                SnackbarHost(
                    snackbarHostState,
                    modifier = Modifier.animateContentSize()
                )
            },
            containerColor = MaterialTheme.colorScheme.surfaceContainer
        ) {
            Column(
                Modifier.padding(it).fillMaxSize().padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                Text("Customize UI Colors", style = MaterialTheme.typography.headlineSmall)
                Text("Primary Color")
                AndroidView(
                    factory = { ctx ->
                        HSLColorPicker(ctx).apply {
                            setColorSelectionListener(object : OnColorSelectionListener {
                                override fun onColorSelectionStart(color: Int) {}
                                override fun onColorSelected(color: Int) {
                                    primaryColor = Color(color)
                                }
                                override fun onColorSelectionEnd(color: Int) {}
                            })
                        }
                    },
                    modifier = Modifier.fillMaxWidth().height(150.dp)
                )

                Text("Background Color")
                AndroidView(
                    factory = { ctx ->
                        HSLColorPicker(ctx).apply {
                            setColorSelectionListener(object : OnColorSelectionListener {
                                override fun onColorSelectionStart(color: Int) {}
                                override fun onColorSelected(color: Int) {
                                    backgroundColor = Color(color)
                                }
                                override fun onColorSelectionEnd(color: Int) {}
                            })
                        }
                    },
                    modifier = Modifier.fillMaxWidth().height(150.dp)
                )

                Text("Surface Color")
                AndroidView(
                    factory = { ctx ->
                        HSLColorPicker(ctx).apply {
                            setColorSelectionListener(object : OnColorSelectionListener {
                                override fun onColorSelectionStart(color: Int) {}
                                override fun onColorSelected(color: Int) {
                                    surfaceColor = Color(color)
                                }
                                override fun onColorSelectionEnd(color: Int) {}
                            })
                        }
                    },
                    modifier = Modifier.fillMaxWidth().height(150.dp)
                )

                Button(onClick = {
                    saveColors(prefs, primaryColor, backgroundColor, surfaceColor)
                }) {
                    Text("Save Colors")
                }
            }
        }
    }
}

fun getStoredColor(prefs: SharedPreferences, key: String, defaultColor: Color): Color {
    return Color(prefs.getInt(key, defaultColor.toArgb()))
}

fun saveColors(prefs: SharedPreferences, primary: Color, background: Color, surface: Color) {
    prefs.edit().apply {
        putInt("primaryColor", primary.toArgb())
        putInt("backgroundColor", background.toArgb())
        putInt("surfaceColor", surface.toArgb())
        apply()
    }
}

