package com.example.noti_u.data.repository

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.noti_u.data.model.Recordatorios
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
class RecordatoriosRepositoryInstrumentedTest {

    private lateinit var repository: RecordatoriosRepository
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseDatabase
    private lateinit var userId: String

    @Before
    fun setUp() = runBlocking {
        // 1. Inicializar instancias
        auth = FirebaseAuth.getInstance()
        db = FirebaseDataSource.database // Usamos la instancia del DataSource o FirebaseDatabase.getInstance()
        repository = RecordatoriosRepository()

        // 2. Iniciar sesión anónima para tener permisos de escritura
        val authResult = auth.signInAnonymously().await()
        userId = authResult.user?.uid ?: throw Exception("Error al obtener usuario de prueba")

        // 3. Limpiar la base de datos de recordatorios para este usuario
        limpiarRecordatoriosUsuario()
    }

    @After
    fun tearDown() = runBlocking {
        // Limpieza posterior y cierre de sesión
        limpiarRecordatoriosUsuario()
        auth.signOut()
    }

    private suspend fun limpiarRecordatoriosUsuario() {
        // La ruta en tu repositorio es "recordatorios/{userId}"
        db.reference.child("recordatorios").child(userId).removeValue().await()
    }

    // --- TEST 1: Guardar y Consultar (Create & Read) ---
    @Test
    fun testGuardarYConsultarRecordatorio() = runBlocking {
        // GIVEN: Generamos un ID manual para poder verificar la inserción
        val idGenerado = db.reference.push().key ?: "id_rec_test"

        val nuevoRecordatorio = Recordatorios(
            id = idGenerado,
            nombre = "Recordatorio Médico",
            descripcion = "Cita con el dentista",
            fecha = "15/11/2024",
            hora = "10:00"
            // Agrega otros campos si tu modelo los tiene
        )

        // WHEN: Guardamos el recordatorio
        repository.guardarRecordatorio(userId, nuevoRecordatorio)

        // THEN: Consultamos directamente
        val recordatorioRecuperado = repository.consultarRecordatorio(userId, idGenerado)

        assertNotNull("El recordatorio debería existir en la BD", recordatorioRecuperado)
        assertEquals("El nombre debe coincidir", nuevoRecordatorio.nombre, recordatorioRecuperado?.nombre)
        assertEquals("La fecha debe coincidir", nuevoRecordatorio.fecha, recordatorioRecuperado?.fecha)
    }

    // --- TEST 2: Editar Recordatorio (Update) ---
    @Test
    fun testEditarRecordatorio() = runBlocking {
        // GIVEN: Un recordatorio existente
        val idGenerado = "id_edit_test"
        val recordatorioOriginal = Recordatorios(
            id = idGenerado,
            nombre = "Original",
            descripcion = "Sin cambios",
            fecha = "01/01/2024",
            hora = "08:00"
        )
        repository.guardarRecordatorio(userId, recordatorioOriginal)

        // WHEN: Modificamos datos y llamamos a editar
        val recordatorioEditado = recordatorioOriginal.copy(
            nombre = "Editado",
            hora = "09:30"
        )
        repository.editarRecordatorio(userId, recordatorioEditado)

        // THEN: Verificamos que los cambios se reflejen
        val resultado = repository.consultarRecordatorio(userId, idGenerado)

        assertNotNull(resultado)
        assertEquals("El nombre debería haber cambiado", "Editado", resultado?.nombre)
        assertEquals("La hora debería haber cambiado", "09:30", resultado?.hora)
        // La descripción no debió cambiar
        assertEquals("La descripción se mantiene", "Sin cambios", resultado?.descripcion)
    }

    // --- TEST 3: Eliminar Recordatorio (Delete) ---
    @Test
    fun testEliminarRecordatorio() = runBlocking {
        // GIVEN: Guardamos un recordatorio
        val idGenerado = "id_delete_test"
        val recordatorio = Recordatorios(
            id = idGenerado,
            nombre = "A borrar",
            descripcion = "...",
            fecha = "...",
            hora = "..."
        )
        repository.guardarRecordatorio(userId, recordatorio)

        // Verificamos existencia previa
        assertNotNull(repository.consultarRecordatorio(userId, idGenerado))

        // WHEN: Eliminamos
        repository.eliminarRecordatorio(userId, idGenerado)

        // THEN: Debe retornar null al consultar
        val resultado = repository.consultarRecordatorio(userId, idGenerado)
        assertNull("El recordatorio no debería existir tras eliminarlo", resultado)
    }
}