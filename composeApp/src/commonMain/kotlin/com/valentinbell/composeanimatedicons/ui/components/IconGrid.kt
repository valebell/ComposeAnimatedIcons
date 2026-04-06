package com.valentinbell.composeanimatedicons.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import icons.IconMeta

@Composable
fun IconGrid(
    icons: List<IconMeta>,
    onIconClick: (IconMeta) -> Unit,
    selectedIcon: IconMeta? = null,
    modifier: Modifier = Modifier
) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 120.dp),
        modifier = modifier,
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(icons) { icon ->
            IconCard(
                icon = icon,
                onClick = { onIconClick(icon) },
                isSelected = icon == selectedIcon
            )
        }
    }
}