package com.valentinbell.composeanimatedicons.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Language
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import composeanimatedicons.composeapp.generated.resources.Res
import composeanimatedicons.composeapp.generated.resources.language_english
import composeanimatedicons.composeapp.generated.resources.language_french
import composeanimatedicons.composeapp.generated.resources.language_switcher
import composeanimatedicons.composeapp.generated.resources.language_thai
import org.jetbrains.compose.resources.stringResource

@Composable
fun LanguageSwitcher(
    currentLanguage: String,
    onLanguageChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var showLanguageMenu by remember { mutableStateOf(false) }

    Box(modifier = modifier) {
        IconButton(
            onClick = { showLanguageMenu = true },
        ) {
            Icon(
                imageVector = Icons.Filled.Language,
                contentDescription = stringResource(Res.string.language_switcher),
                tint = MaterialTheme.colorScheme.primary
            )
        }

        DropdownMenu(
            expanded = showLanguageMenu,
            onDismissRequest = { showLanguageMenu = false },
            modifier = Modifier.widthIn(min = 180.dp)
        ) {
            LanguageOption(
                languageCode = "en",
                languageName = stringResource(Res.string.language_english),
                currentLanguage = currentLanguage,
                onLanguageSelected = { language ->
                    onLanguageChange(language)
                    showLanguageMenu = false
                }
            )
            LanguageOption(
                languageCode = "fr",
                languageName = stringResource(Res.string.language_french),
                currentLanguage = currentLanguage,
                onLanguageSelected = { language ->
                    onLanguageChange(language)
                    showLanguageMenu = false
                }
            )
            LanguageOption(
                languageCode = "th",
                languageName = stringResource(Res.string.language_thai),
                currentLanguage = currentLanguage,
                onLanguageSelected = { language ->
                    onLanguageChange(language)
                    showLanguageMenu = false
                }
            )
        }
    }
}

@Composable
private fun LanguageOption(
    languageCode: String,
    languageName: String,
    currentLanguage: String,
    onLanguageSelected: (String) -> Unit
) {
    DropdownMenuItem(
        text = { Text(languageName) },
        onClick = { onLanguageSelected(languageCode) },
        trailingIcon = {
            if (languageCode == currentLanguage) {
                Icon(
                    imageVector = Icons.Filled.Language,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
    )
}