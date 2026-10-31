package com.example.ui.theme

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val GridironColorScheme = darkColorScheme(
    primary = ElectricGold,
    onPrimary = Color(0xFF1F1200),
    primaryContainer = Color(0xFF3E2800),
    onPrimaryContainer = GoldLight,
    secondary = CyanAccent,
    onSecondary = Color(0xFF00363D),
    secondaryContainer = Color(0xFF004F59),
    onSecondaryContainer = Color(0xFF70F5FF),
    tertiary = TurfGreen,
    onTertiary = Color(0xFF003822),
    tertiaryContainer = Color(0xFF005234),
    onTertiaryContainer = TurfGreenLight,
    error = FieryRed,
    onError = Color.White,
    background = BackgroundDark,
    onBackground = TextPrimaryDark,
    surface = SurfaceDark,
    onSurface = TextPrimaryDark,
    surfaceVariant = SurfaceVariantDark,
    onSurfaceVariant = TextSecondaryDark,
    outline = BorderLight,
    outlineVariant = BorderHighlight
)

@Composable
fun MyApplicationTheme(
    content: @Composable () -> Unit
) {
    val colorScheme = GridironColorScheme
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as? Activity)?.window
            if (window != null) {
                window.statusBarColor = BackgroundDark.toArgb()
                window.navigationBarColor = BackgroundDark.toArgb()
                val controller = WindowCompat.getInsetsController(window, view)
                controller.isAppearanceLightStatusBars = false
                controller.isAppearanceLightNavigationBars = false
            }
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
