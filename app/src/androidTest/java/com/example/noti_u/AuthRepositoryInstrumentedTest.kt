package com.example.noti_u.data.repository

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.noti_u.utils.FirebaseDataSource
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.UUID

@RunWith(AndroidJUnit4::class)
class AuthRepositoryInstrumentedTest {

    private lateinit var repository: AuthRepository
    private lateinit var auth: FirebaseAuth

    @Before
    fun setUp() {
        // En pruebas instrumentadas, usamos la instancia REAL de Firebase
        // Asegúrate de que tu google-services.json esté bien configurado en la app
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        auth = FirebaseDataSource.auth

        // Inicializamos el repositorio real
        repository = AuthRepository()
    }

    @Test
    fun testRegistroLoginFlujoCompleto() = runBlocking {
        // --- 1. PREPARACIÓN (GIVEN) ---
        // Generamos un email único para no chocar con usuarios existentes
        // Ejemplo: test-12345678-abcd...@prueba.com
        val randomString = UUID.randomUUID().toString().substring(0, 8)
        val email = "test-$randomString@prueba.com"
        val password = "Password123!"

        // --- 2. ACCIÓN DE REGISTRO (WHEN) ---
        val resultadoRegistro = repository.register(email, password)

        // --- 3. VERIFICACIÓN REGISTRO (THEN) ---
        assertTrue("El registro debería ser exitoso", resultadoRegistro.isSuccess)
        val uid = resultadoRegistro.getOrNull()
        assertNotNull("El UID no debería ser nulo", uid)

        // Cerramos sesión para probar el login limpio
        repository.logout()

        // --- 4. ACCIÓN DE LOGIN (WHEN) ---
        val resultadoLogin = repository.login(email, password)

        // --- 5. VERIFICACIÓN LOGIN (THEN) ---
        assertTrue("El login debería ser exitoso con las credenciales creadas", resultadoLogin.isSuccess)
        val user = resultadoLogin.getOrNull()

        assertNotNull(user)
        assertEquals("El correo del usuario logueado debe coincidir", email, user?.correo)
        assertEquals("El ID del usuario logueado debe coincidir con el registro", uid, user?.id)

        // LIMPIEZA (Opcional pero recomendada): Borrar el usuario creado para no ensuciar Firebase
        // auth.currentUser?.delete()
    }

    @Test
    fun testLoginFallidoConCredencialesIncorrectas() = runBlocking {
        // GIVEN
        val email = "noexiste@prueba.com"
        val password = "clavefalsa123"

        // WHEN
        val result = repository.login(email, password)

        // THEN
        assertTrue("El login debería fallar", result.isFailure)
        assertNotNull("Debería haber una excepción", result.exceptionOrNull())
    }
}