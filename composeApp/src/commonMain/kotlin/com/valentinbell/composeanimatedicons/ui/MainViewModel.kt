package com.valentinbell.composeanimatedicons.ui

import androidx.lifecycle.ViewModel
import com.valentinbell.composeanimatedicons.repository.IconRepository
import icons.IconMeta
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class MainViewModel(
    repository: IconRepository = IconRepository(),
) : ViewModel() {
    private val _icons = MutableStateFlow<List<IconMeta>>(emptyList())
    val icons: StateFlow<List<IconMeta>> = _icons.asStateFlow()

    init {
        _icons.value = repository.getAllIcons()
    }
}