package com.example.rickandmorty.ui.theme

// Colors.kt
import android.app.Activity
import androidx.compose.ui.graphics.Color
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

// your only solid colour
val RickPrimaryLight = Color(0xFF4D586B)    // a little lighter for containers
val RickPrimaryDark  = Color(0xFF1A1F2B)    // a little darker for depth

// Text colours that are always readable on RickPrimary
private val OnRickPrimary     = Color(0xFFE6E6E6)   // light gray
private val OnRickPrimaryDim  = Color(0xFFB3B3B3)   // dimmed for secondary text

private val RickColorScheme = darkColorScheme(
    primary               = RickPrimary,
    onPrimary             = Color.White,
    primaryContainer      = RickPrimaryLight,
    onPrimaryContainer    = Color.White,

    secondary             = RickPrimaryDark,
    onSecondary           = Color.White,
    secondaryContainer    = RickPrimaryLight,
    onSecondaryContainer  = Color.White,

    // Background = your solid colour
    background            = RickPrimary,
    onBackground          = OnRickPrimary,

    surface               = RickPrimary,
    onSurface             = OnRickPrimary,

    surfaceVariant        = RickPrimaryLight,
    onSurfaceVariant      = OnRickPrimaryDim,

    error                 = Color(0xFFCF6679),
    onError               = Color.Black
)


@Composable
fun RickAndMortyTheme(
    content: @Composable () -> Unit
) {
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = RickPrimary.toArgb()
            WindowCompat.getInsetsController(window, view)
                .isAppearanceLightStatusBars = false   // dark icons on dark bg
        }
    }

    MaterialTheme(
        colorScheme = RickColorScheme,
        typography = Typography,   // keep your existing Typography
        content = content
    )
}