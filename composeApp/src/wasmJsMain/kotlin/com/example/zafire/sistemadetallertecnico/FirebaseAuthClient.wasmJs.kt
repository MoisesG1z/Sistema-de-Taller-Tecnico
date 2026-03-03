package com.example.zafire.sistemadetallertecnico

actual object FirebaseAuthClient {
    actual fun isConfigured(): Boolean = false

    actual fun hasActiveSession(): Boolean = false

    actual fun signIn(email: String, password: String, onResult: (error: String?) -> Unit) {
        onResult("Firebase Auth esta implementado para target JS. Ejecuta jsBrowserDevelopmentRun.")
    }

    actual fun signUp(email: String, password: String, onResult: (error: String?) -> Unit) {
        onResult("Firebase Auth esta implementado para target JS. Ejecuta jsBrowserDevelopmentRun.")
    }

    actual fun signOut() = Unit
}
