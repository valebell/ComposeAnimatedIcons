package com.valentinbell.composeanimatedicons

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.valentinbell.composeanimatedicons.ui.MainRoute
import com.valentinbell.composeanimatedicons.ui.theme.AppLocalLanguageEnvironment
import com.valentinbell.composeanimatedicons.ui.theme.AppTheme
import com.valentinbell.composeanimatedicons.ui.theme.customAppLocale

@Composable
fun App() {
    var isDarkTheme by remember { mutableStateOf(false) }

    AppLocalLanguageEnvironment {
        AppTheme(isDarkTheme = isDarkTheme) {
            MainRoute(
                isDarkTheme = isDarkTheme,
                onThemeChange = { isDark ->
                    isDarkTheme = isDark
                },
                currentLanguage = customAppLocale,
                onLanguageChange = { language ->
                    customAppLocale = language
                }
            )
        }
    }
}