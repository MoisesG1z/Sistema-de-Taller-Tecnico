package com.example.zafire.sistemadetallertecnico

expect object FirebaseAuthClient {
    fun isConfigured(): Boolean
    fun hasActiveSession(): Boolean
    fun signIn(email: String, password: String, onResult: (error: String?) -> Unit)
    fun signUp(email: String, password: String, onResult: (error: String?) -> Unit)
    fun signOut()
}
