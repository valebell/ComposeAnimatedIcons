package com.valentinbell.composeanimatedicons.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import composeanimatedicons.composeapp.generated.resources.Res
import composeanimatedicons.composeapp.generated.resources.dark_theme
import composeanimatedicons.composeapp.generated.resources.light_theme
import org.jetbrains.compose.resources.stringResource

@Composable
fun ThemeSwitcher(
    isDarkTheme: Boolean,
    onThemeChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    IconButton(
        onClick = { onThemeChange(!isDarkTheme) },
        modifier = modifier
    ) {
        Icon(
            imageVector = if (isDarkTheme) {
                Icons.Filled.LightMode
            } else {
                Icons.Filled.DarkMode
            },
            contentDescription = if (isDarkTheme) stringResource(Res.string.light_theme) else stringResource(Res.string.dark_theme),
            tint = MaterialTheme.colorScheme.primary
        )
    }
}