package com.lk.memobar2.ui

import android.content.Context
import android.os.Build
import android.view.Window
import androidx.annotation.RequiresApi
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat

/**
 * Erstellt von Lena am 28/04/2019.
 */
object Themes {

    val AppBarFontWeight = FontWeight.Companion.Normal

    @RequiresApi(Build.VERSION_CODES.S)
    @Composable
    fun getMaterialColorScheme(context: Context): ColorScheme {
        // we want a dynamic color scheme but need to determine ourselvese if it's light or dark
        val darkTheme = isSystemInDarkTheme()
        return if (darkTheme) {
            dynamicDarkColorScheme(context)
        } else {
            dynamicLightColorScheme(context)
        }
    }

    @Composable
    fun setStatusBarIconColours(window: Window) {
        val darkTheme = isSystemInDarkTheme()
        if (darkTheme) {
            WindowCompat.getInsetsController(window, window.decorView)
                .isAppearanceLightStatusBars = false
        } else {
            WindowCompat.getInsetsController(window, window.decorView)
                .isAppearanceLightStatusBars = true
        }
    }

    @Composable
    fun getTopBarColors(): TopAppBarColors {
        return TopAppBarDefaults.topAppBarColors(containerColor = getAppBackgroundColor(),
            scrolledContainerColor = MaterialTheme.colorScheme.surfaceContainer)
    }
    // change both together
    @Composable
    fun getAppBackgroundColor(): Color = MaterialTheme.colorScheme.surface


    @Composable
    fun getDropdownMenuBackground(): Color = MaterialTheme.colorScheme.surfaceContainerHighest

    @Composable
    fun getListCardColor(): Color = MaterialTheme.colorScheme.surfaceContainerLow

    @Composable
    fun getOutlinedTextFieldColors(): TextFieldColors {
        return OutlinedTextFieldDefaults.colors(
            focusedTextColor = getDefaultTextColor(),
            unfocusedTextColor = getDefaultTextColor()
        )
    }

    @Composable
    fun getDefaultTextColor(): Color {
        return if(isSystemInDarkTheme()) {
            MaterialTheme.colorScheme.onSurfaceVariant
        } else {
            MaterialTheme.colorScheme.onSurface
        }
    }

    @Composable
    fun getCardSettings(showCards: Boolean): ComposeCardSettings {
        return if (showCards) {
            ComposeCardSettings(
                cardColor = getListCardColor(),
                cardPadding = Modifier.Companion.padding(start = 5.dp, end = 5.dp, top = 0.dp, bottom = 12.dp),
                textPadding =  Modifier.Companion.padding(14.dp),
                textColor = getDefaultTextColor() )
        } else {
            ComposeCardSettings(
                cardColor = getAppBackgroundColor(),
                cardPadding = Modifier.Companion.padding(start = 4.dp),
                textPadding =  Modifier.Companion.padding(8.dp),
                textColor = getDefaultTextColor() )
        }
    }
}

data class ComposeCardSettings (val cardColor: Color, val cardPadding: Modifier, val textPadding: Modifier,
                                val textColor: Color
)