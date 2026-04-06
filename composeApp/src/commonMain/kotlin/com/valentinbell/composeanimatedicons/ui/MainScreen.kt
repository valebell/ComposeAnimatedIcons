package com.valentinbell.composeanimatedicons.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.valentinbell.composeanimatedicons.repository.IconRepository
import com.valentinbell.composeanimatedicons.ui.components.IconDetailSheet
import com.valentinbell.composeanimatedicons.ui.components.IconGrid
import com.valentinbell.composeanimatedicons.ui.components.LanguageSwitcher
import com.valentinbell.composeanimatedicons.ui.components.ThemeSwitcher
import composeanimatedicons.composeapp.generated.resources.Res
import composeanimatedicons.composeapp.generated.resources.grid_description
import composeanimatedicons.composeapp.generated.resources.grid_title
import icons.IconMeta
import org.jetbrains.compose.resources.stringResource

@Composable
fun MainRoute(
    isDarkTheme: Boolean,
    onThemeChange: (Boolean) -> Unit,
    currentLanguage: String,
    onLanguageChange: (String) -> Unit
) {
    val viewModel: MainViewModel = viewModel(
        factory = viewModelFactory {
            initializer {
                MainViewModel(IconRepository())
            }
        }
    )

    val icons by viewModel.icons.collectAsState()

    MainScreenContent(
        icons = icons,
        isDarkTheme = isDarkTheme,
        onThemeChange = onThemeChange,
        currentLanguage = currentLanguage,
        onLanguageChange = onLanguageChange
    )
}

@Composable
fun MainScreenContent(
    icons: List<IconMeta>,
    isDarkTheme: Boolean,
    onThemeChange: (Boolean) -> Unit,
    currentLanguage: String,
    onLanguageChange: (String) -> Unit
) {
    var selectedIcon by rememberSaveable { mutableStateOf<IconMeta?>(null) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .safeContentPadding()
            .background(MaterialTheme.colorScheme.background)
    ) {
        MainContent(
            allIcons = icons,
            selectedIcon = selectedIcon,
            onIconSelected = { icon ->
                selectedIcon = if (selectedIcon == icon) null else icon
            }
        )

        ThemeSwitcher(
            isDarkTheme = isDarkTheme,
            onThemeChange = onThemeChange,
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(16.dp)
        )

        LanguageSwitcher(
            currentLanguage = currentLanguage,
            onLanguageChange = onLanguageChange,
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(start = 72.dp, bottom = 16.dp)
        )

        IconDetailSheet(
            iconMeta = selectedIcon,
            isVisible = selectedIcon != null,
            onDismiss = { selectedIcon = null }
        )
    }
}

@Composable
private fun MainContent(
    allIcons: List<IconMeta>,
    selectedIcon: IconMeta?,
    onIconSelected: (IconMeta) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 32.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HeaderSection()
        IconGrid(
            icons = allIcons,
            onIconClick = onIconSelected,
            selectedIcon = selectedIcon,
            modifier = Modifier.widthIn(max = 800.dp)
        )
    }
}

@Composable
private fun HeaderSection() {
    Column(
        modifier = Modifier.widthIn(max = 800.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            modifier = Modifier.padding(horizontal = 16.dp),
            text = stringResource(Res.string.grid_title),
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onSurface
        )

        Text(
            modifier = Modifier.padding(horizontal = 16.dp),
            text = stringResource(Res.string.grid_description),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}
