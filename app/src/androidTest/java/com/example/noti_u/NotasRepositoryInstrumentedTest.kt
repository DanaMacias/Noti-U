package com.example.noti_u.data.repository

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.noti_u.data.model.Notas
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
class NotasRepositoryInstrumentedTest {

    private lateinit var repository: NotasRepository
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseDatabase
    private lateinit var userId: String

    @Before
    fun setUp() = runBlocking {
        // 1. Inicializar instancias
        auth = FirebaseAuth.getInstance()
        db = FirebaseDatabase.getInstance() // O FirebaseDataSource.database si prefieres
        repository = NotasRepository()

        // 2. Iniciar sesión anónima para tener permisos de escritura
        val authResult = auth.signInAnonymously().await()
        userId = authResult.user?.uid ?: throw Exception("No se pudo obtener usuario de prueba")

        // 3. Limpiar la base de datos de notas para este usuario antes de empezar
        limpiarNotasUsuario()
    }

    @After
    fun tearDown() = runBlocking {
        // Limpieza posterior
        limpiarNotasUsuario()
        auth.signOut()
    }

    private suspend fun limpiarNotasUsuario() {
        db.reference.child("notas").child(userId).removeValue().await()
    }

    // --- TEST 1: Guardar y Consultar (Create & Read) ---
    @Test
    fun testGuardarYConsultarNota() = runBlocking {
        // GIVEN: Creamos una nota.
        // TRUCO: Generamos el ID aquí para poder buscarla luego, ya que guardarNota no retorna el ID.
        val idGenerado = db.reference.push().key ?: "id_test"

        val nuevaNota = Notas(
            id = idGenerado,
            titulo = "Nota de Prueba",
            descripcion = "Contenido de la nota de prueba",

        )

        // WHEN: Guardamos la nota
        repository.guardarNota(userId, nuevaNota)

        // THEN: Consultamos la nota directamente de Firebase para ver si existe
        val notaRecuperada = repository.consultarNota(userId, idGenerado)

        assertNotNull("La nota debería existir en la base de datos", notaRecuperada)
        assertEquals("El título debe coincidir", nuevaNota.titulo, notaRecuperada?.titulo)
        assertEquals("La descripción debe coincidir", nuevaNota.descripcion, notaRecuperada?.descripcion)
    }

    // --- TEST 2: Editar Nota (Update) ---
    @Test
    fun testEditarNota() = runBlocking {
        // GIVEN: Una nota ya guardada
        val idGenerado = "id_para_editar"
        val notaOriginal = Notas(
            id = idGenerado,
            titulo = "Original",
            descripcion = "Texto original",

        )
        repository.guardarNota(userId, notaOriginal)

        // WHEN: Modificamos el objeto y llamamos a editar
        val notaEditada = notaOriginal.copy(
            titulo = "Editado",
            descripcion = "Texto cambiado"
        )
        repository.editarNota(userId, notaEditada)

        // THEN: Recuperamos y verificamos los cambios
        val notaFinal = repository.consultarNota(userId, idGenerado)

        assertNotNull(notaFinal)
        assertEquals("El título debería haber cambiado", "Editado", notaFinal?.titulo)
        assertEquals("La descripción debería haber cambiado", "Texto cambiado", notaFinal?.descripcion)
    }

    // --- TEST 3: Eliminar Nota (Delete) ---
    @Test
    fun testEliminarNota() = runBlocking {
        // GIVEN: Una nota guardada
        val idGenerado = "id_para_borrar"
        val nota = Notas(
            id = idGenerado,
            titulo = "A borrar",
            descripcion = "...",

        )
        repository.guardarNota(userId, nota)

        // Verificamos que se guardó primero (Sanity check)
        assertNotNull(repository.consultarNota(userId, idGenerado))

        // WHEN: Eliminamos la nota
        repository.eliminarNota(userId, idGenerado)

        // THEN: Al intentar consultarla, debe retornar null
        val resultado = repository.consultarNota(userId, idGenerado)
        assertNull("La nota no debería existir después de eliminarse", resultado)
    }
}