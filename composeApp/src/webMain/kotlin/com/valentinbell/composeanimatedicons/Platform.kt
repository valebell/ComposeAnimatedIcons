package com.valentinbell.composeanimatedicons

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform