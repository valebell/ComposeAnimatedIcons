package com.valentinbell.composeanimatedicons.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import composeanimatedicons.composeapp.generated.resources.Res
import composeanimatedicons.composeapp.generated.resources.close
import composeanimatedicons.composeapp.generated.resources.code
import composeanimatedicons.composeapp.generated.resources.copied
import composeanimatedicons.composeapp.generated.resources.copy
import composeanimatedicons.composeapp.generated.resources.kotlin_compose
import icons.IconMeta
import icons.iconCode
import icons.renderIcon
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.stringResource

@Composable
fun IconDetailSheet(
    iconMeta: IconMeta?,
    onDismiss: () -> Unit,
    isVisible: Boolean = false,
    modifier: Modifier = Modifier
) {
    var showCopiedMessage by remember { mutableStateOf(false) }

    Box(modifier = modifier.fillMaxSize()) {
        AnimatedVisibility(
            visible = isVisible,
            enter = slideInHorizontally(
                animationSpec = tween(durationMillis = 300),
                initialOffsetX = { fullWidth -> fullWidth }
            ),
            exit = slideOutHorizontally(
                animationSpec = tween(durationMillis = 300),
                targetOffsetX = { fullWidth -> fullWidth }
            ),
            modifier = Modifier.align(Alignment.CenterEnd)
        ) {
            Surface(
                modifier = Modifier
                    .width(400.dp)
                    .fillMaxHeight()
                    .align(Alignment.TopEnd),
                shape = RoundedCornerShape(topStart = 16.dp, bottomStart = 16.dp),
                tonalElevation = 8.dp
            ) {
                iconMeta?.let {
                    IconDetailContent(
                        iconMeta = it,
                        onDismiss = onDismiss,
                        showCopiedMessage = showCopiedMessage,
                        onShowCopiedMessage = { message -> showCopiedMessage = message }
                    )
                }
            }
        }
    }
}

@Composable
private fun IconDetailContent(
    iconMeta: IconMeta,
    onDismiss: () -> Unit,
    showCopiedMessage: Boolean,
    onShowCopiedMessage: (Boolean) -> Unit
) {
    val clipboardManager: ClipboardManager = LocalClipboardManager.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.End),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onDismiss,
                modifier = Modifier.size(40.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Close,
                    contentDescription = stringResource(Res.string.close),
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
        }

        Text(
            text = iconMeta.label,
            style = MaterialTheme.typography.headlineMedium
        )

        Text(
            text = stringResource(iconMeta.descriptionRes),
            style = MaterialTheme.typography.titleMedium
        )

        Row(
            modifier = Modifier.fillMaxWidth().height(200.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.End),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                renderIcon(
                    iconMeta.id,
                    tint = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.size(100.dp),
                    animate = true
                )
            }
        }

        CodeSnippetSection(
            iconMeta = iconMeta,
            clipboardManager = clipboardManager,
            showCopiedMessage = showCopiedMessage,
            onShowCopiedMessage = onShowCopiedMessage
        )
    }
}

@Composable
private fun CodeSnippetSection(
    iconMeta: IconMeta,
    clipboardManager: ClipboardManager,
    showCopiedMessage: Boolean,
    onShowCopiedMessage: (Boolean) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surfaceContainer)
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outlineVariant,
                shape = RoundedCornerShape(16.dp)
            )
            .padding(16.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Code,
                        contentDescription = stringResource(Res.string.code),
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(24.dp)
                    )
                    Text(
                        text = stringResource(Res.string.kotlin_compose),
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                LaunchedEffect(showCopiedMessage) {
                    if (showCopiedMessage) {
                        delay(2000)
                        onShowCopiedMessage(false)
                    }
                }

                CopyButton(
                    showCopiedMessage = showCopiedMessage,
                    onClick = {
                        if (!showCopiedMessage) {
                            clipboardManager.setText(AnnotatedString(iconCode(iconMeta.id)))
                            onShowCopiedMessage(true)
                        }
                    }
                )
            }

            Text(
                text = iconCode(iconMeta.id),
                style = MaterialTheme.typography.bodyMedium.copy(MaterialTheme.colorScheme.onSurface),
            )
        }
    }
}

@Composable
private fun CopyButton(
    showCopiedMessage: Boolean,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier.height(44.dp).padding(4.dp),
        shape = RoundedCornerShape(8.dp),
        enabled = !showCopiedMessage
    ) {
        if (showCopiedMessage) {
            Icon(
                imageVector = Icons.Filled.ContentCopy,
                contentDescription = stringResource(Res.string.copied),
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = stringResource(Res.string.copied),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.primary
            )
        } else {
            Icon(
                imageVector = Icons.Filled.ContentCopy,
                contentDescription = stringResource(Res.string.copy),
                tint = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = stringResource(Res.string.copy),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}