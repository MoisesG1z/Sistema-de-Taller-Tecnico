package com.example.zafire.sistemadetallertecnico

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform