package com.mucheng.mucute.client.overlay

import android.content.Context
import android.content.SharedPreferences
import android.view.WindowManager
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.edit
import com.madrapps.pikolo.HSLColorPicker
import com.madrapps.pikolo.listeners.OnColorSelectionListener

class OverlayColorPicker : OverlayWindow() {

    private val _layoutParams by lazy {
        super.layoutParams.apply {
            width = WindowManager.LayoutParams.MATCH_PARENT
            height = WindowManager.LayoutParams.MATCH_PARENT
        }
    }

    override val layoutParams: WindowManager.LayoutParams
        get() = _layoutParams

    @Composable
    override fun Content() {
        val context = LocalContext.current
        val prefs: SharedPreferences = context.getSharedPreferences("theme_prefs", Context.MODE_PRIVATE)
        val defaultPrimaryColor = MaterialTheme.colorScheme.primary
        val defaultBackgroundColor = MaterialTheme.colorScheme.background
        val defaultSurfaceColor = MaterialTheme.colorScheme.surface

        var selectedPrimaryColor by remember { mutableStateOf(getStoredColor(prefs, "primaryColor", defaultPrimaryColor)) }
        var selectedBackgroundColor by remember { mutableStateOf(getStoredColor(prefs, "backgroundColor", defaultBackgroundColor)) }
        var selectedSurfaceColor by remember { mutableStateOf(getStoredColor(prefs, "surfaceColor", defaultSurfaceColor)) }

        Column(
            Modifier.fillMaxSize().padding(20.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text("Select Primary Color")
            AndroidView(
                factory = { ctx ->
                    HSLColorPicker(ctx).apply {
                        setColorSelectionListener(object : OnColorSelectionListener {
                            override fun onColorSelectionStart(color: Int) {}
                            override fun onColorSelected(color: Int) {
                                selectedPrimaryColor = Color(color)
                            }
                            override fun onColorSelectionEnd(color: Int) {}
                        })
                    }
                },
                modifier = Modifier.fillMaxWidth().height(150.dp)
            )

            Text("Select Background Color")
            AndroidView(
                factory = { ctx ->
                    HSLColorPicker(ctx).apply {
                        setColorSelectionListener(object : OnColorSelectionListener {
                            override fun onColorSelectionStart(color: Int) {}
                            override fun onColorSelected(color: Int) {
                                selectedBackgroundColor = Color(color)
                            }
                            override fun onColorSelectionEnd(color: Int) {}
                        })
                    }
                },
                modifier = Modifier.fillMaxWidth().height(150.dp)
            )

            Text("Select Surface Color")
            AndroidView(
                factory = { ctx ->
                    HSLColorPicker(ctx).apply {
                        setColorSelectionListener(object : OnColorSelectionListener {
                            override fun onColorSelectionStart(color: Int) {}
                            override fun onColorSelected(color: Int) {
                                selectedSurfaceColor = Color(color)
                            }
                            override fun onColorSelectionEnd(color: Int) {}
                        })
                    }
                },
                modifier = Modifier.fillMaxWidth().height(150.dp)
            )

            Button(onClick = {
                prefs.edit {
                    putInt("primaryColor", selectedPrimaryColor.toArgb())
                    putInt("backgroundColor", selectedBackgroundColor.toArgb())
                    putInt("surfaceColor", selectedSurfaceColor.toArgb())
                }
                OverlayManager.dismissOverlayWindow(this@OverlayColorPicker)
            }) {
                Text("Apply Colors", color = MaterialTheme.colorScheme.onPrimary)
            }


        }
    }

    private fun getStoredColor(prefs: SharedPreferences, key: String, defaultColor: Color): Color {
        return Color(prefs.getInt(key, defaultColor.value.toInt()))
    }
}
