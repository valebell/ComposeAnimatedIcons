package com.valentinbell.composeanimatedicons.repository

import icons.IconMeta
import icons.iconRegistry

class IconRepository {
    fun getAllIcons(): List<IconMeta> {
        return iconRegistry
    }
}