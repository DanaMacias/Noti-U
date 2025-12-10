package com.example.noti_u.data.repository

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.noti_u.data.model.User
import com.example.noti_u.utils.FirebaseDataSource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class UserRepositoryInstrumentedTest {

    private lateinit var repository: UserRepository
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseDatabase
    private lateinit var testUserId: String

    @Before
    fun setUp() = runBlocking {
        // 1. Inicializar instancias
        auth = FirebaseAuth.getInstance()
        // Asegúrate de que FirebaseDataSource.database esté inicializado o usa getInstance()
        db = FirebaseDatabase.getInstance()
        repository = UserRepository()

        // 2. Iniciar sesión anónima para tener un UID válido y permisos de escritura
        val authResult = auth.signInAnonymously().await()
        testUserId = authResult.user?.uid ?: throw Exception("Fallo al obtener usuario de prueba")

        // 3. Limpiar datos previos de este usuario para asegurar un test limpio
        limpiarDatosUsuario()
    }

    @After
    fun tearDown() = runBlocking {
        // Limpieza y cierre
        limpiarDatosUsuario()
        auth.signOut()
    }

    private suspend fun limpiarDatosUsuario() {
        // Borramos el nodo específico del usuario de prueba en "users"
        db.reference.child("users").child(testUserId).removeValue().await()
    }

    // --- TEST 1: Guardar y Leer Usuario (Create & Read) ---
    @Test
    fun testSaveAndGetUserData() = runBlocking {
        // GIVEN: Un objeto User completo
        val newUser = User(
            id = testUserId, // Usamos el ID de la sesión anónima
            nombre = "Usuario Test",
            correo = "test@ejemplo.com",
            telefono = "123456789",
            area = "Ingeniería",
            institucion = "Universidad X",
            periodo = "2024-1",
            fechaInicio = "01/02/2024",
            fechaFin = "30/06/2024",
            duracion = "5 meses"
        )

        // WHEN: Guardamos el usuario
        val saveResult = repository.saveUser(newUser)

        // THEN:
        // 1. Verificamos que el resultado sea exitoso
        assertTrue("El guardado debería ser exitoso", saveResult.isSuccess)

        // 2. Recuperamos los datos de Firebase
        val fetchedUser = repository.getUserData(testUserId)

        // 3. Validamos que los datos coincidan
        assertNotNull("El usuario debería existir en la BD", fetchedUser)
        assertEquals("El nombre debe coincidir", newUser.nombre, fetchedUser?.nombre)
        assertEquals("El correo debe coincidir", newUser.correo, fetchedUser?.correo)
        assertEquals("El área debe coincidir", newUser.area, fetchedUser?.area)
    }

    // --- TEST 2: Actualizar Usuario (Update) ---
    @Test
    fun testUpdateUser() = runBlocking {
        // GIVEN: Un usuario ya guardado
        val initialUser = User(
            id = testUserId,
            nombre = "Nombre Original",
            correo = "original@mail.com"
            // Otros campos opcionales...
        )
        repository.saveUser(initialUser)

        // WHEN: Modificamos el objeto y llamamos a update
        val updatedUser = initialUser.copy(
            nombre = "Nombre Cambiado",
            telefono = "999999999"
        )
        val updateResult = repository.updateUser(updatedUser)

        // THEN:
        assertTrue("La actualización debería ser exitosa", updateResult.isSuccess)

        // Recuperamos y verificamos cambios
        val fetchedUser = repository.getUserData(testUserId)

        assertNotNull(fetchedUser)
        assertEquals("El nombre debería haber cambiado", "Nombre Cambiado", fetchedUser?.nombre)
        assertEquals("El teléfono debería haber cambiado", "999999999", fetchedUser?.telefono)
        // El correo no debió cambiar
        assertEquals("El correo se mantiene", "original@mail.com", fetchedUser?.correo)
    }

    // --- TEST 3: Obtener Usuario Inexistente ---
    @Test
    fun testGetUserReturnsNullIfMissing() = runBlocking {
        // GIVEN: Aseguramos que no hay datos (limpieza hecha en setUp)
        // O usamos un ID aleatorio que seguro no existe
        val randomId = "id_inexistente_12345"

        // WHEN: Intentamos obtener datos
        val result = repository.getUserData(randomId)

        // THEN: Debe retornar null
        assertNull("Si el usuario no existe, debe retornar null", result)
    }
}