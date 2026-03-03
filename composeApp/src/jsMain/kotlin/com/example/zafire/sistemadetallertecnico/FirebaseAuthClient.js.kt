@file:Suppress("UnsafeCastFromDynamic")

package com.example.zafire.sistemadetallertecnico

import kotlin.js.JsName

@JsName("firebase")
external val firebase: dynamic

actual object FirebaseAuthClient {
    private var initialized = false
    private val firebaseConfig = js(
        """({
            apiKey: "AIzaSyC3OlyY6pT1FE3yKwJZhnqHqvQ3bLmGOdY",
            authDomain: "tallersysglz.firebaseapp.com",
            projectId: "tallersysglz",
            storageBucket: "tallersysglz.firebasestorage.app",
            messagingSenderId: "472202315295",
            appId: "1:472202315295:web:0abca2b7b647207874b72a",
            measurementId: "G-KS8VD6718K"
        })"""
    )

    actual fun isConfigured(): Boolean = ensureInitialized()

    actual fun hasActiveSession(): Boolean {
        if (!ensureInitialized()) return false
        return firebase.auth().currentUser != null
    }

    actual fun signIn(email: String, password: String, onResult: (error: String?) -> Unit) {
        if (!ensureInitialized()) {
            onResult("Firebase no esta configurado. Revisa FirebaseAuthClient.js.kt")
            return
        }
        firebase.auth().signInWithEmailAndPassword(email, password)
            .then { _: dynamic -> onResult(null) }
            .catch { error: dynamic -> onResult(mapFirebaseError(error)) }
    }

    actual fun signUp(email: String, password: String, onResult: (error: String?) -> Unit) {
        if (!ensureInitialized()) {
            onResult("Firebase no esta configurado. Revisa FirebaseAuthClient.js.kt")
            return
        }
        firebase.auth().createUserWithEmailAndPassword(email, password)
            .then { _: dynamic -> onResult(null) }
            .catch { error: dynamic -> onResult(mapFirebaseError(error)) }
    }

    actual fun signOut() {
        if (!ensureInitialized()) return
        firebase.auth().signOut()
    }

    private fun ensureInitialized(): Boolean {
        if (initialized) return true
        if (!hasValidConfig()) return false

        val apps = firebase.apps
        if (apps.length == 0) {
            firebase.initializeApp(firebaseConfig)
            try {
                firebase.analytics()
            } catch (_: dynamic) {
                // Ignore analytics initialization errors in restricted environments.
            }
        }
        initialized = true
        return true
    }

    private fun hasValidConfig(): Boolean {
        val apiKey = firebaseConfig.apiKey as? String ?: return false
        return apiKey.isNotBlank()
    }

    private fun mapFirebaseError(error: dynamic): String {
        val code = error?.code as? String ?: ""
        return when (code) {
            "auth/invalid-email" -> "Email invalido."
            "auth/user-disabled" -> "Este usuario fue deshabilitado."
            "auth/user-not-found" -> "Usuario no encontrado."
            "auth/wrong-password" -> "Contrasena incorrecta."
            "auth/email-already-in-use" -> "El email ya esta en uso."
            "auth/weak-password" -> "La contrasena debe tener al menos 6 caracteres."
            else -> (error?.message as? String) ?: "No se pudo completar la autenticacion."
        }
    }
}
