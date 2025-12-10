package com.example.noti_u.data.repository

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.noti_u.data.model.Pendientes
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class PendientesRepositoryInstrumentedTest {

    private lateinit var repository: PendientesRepository
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseDatabase
    private lateinit var userId: String

    @Before
    fun setUp() = runBlocking {
        // 1. Inicializar Firebase
        auth = FirebaseAuth.getInstance()
        db = FirebaseDatabase.getInstance()
        repository = PendientesRepository()

        // 2. Login Anónimo para tener permisos
        val authResult = auth.signInAnonymously().await()
        userId = authResult.user?.uid ?: throw Exception("Error al obtener usuario de prueba")

        // 3. Limpiar la base de datos de pendientes para este usuario
        limpiarPendientesUsuario()
    }

    @After
    fun tearDown() = runBlocking {
        limpiarPendientesUsuario()
        auth.signOut()
    }

    private suspend fun limpiarPendientesUsuario() {
        if (::userId.isInitialized) {
            db.reference.child("usuarios").child(userId).child("pendientes").removeValue().await()
        }
    }

    // --- TEST 1: Guardar Pendiente (Create) ---
    @Test
    fun testGuardarPendiente() = runBlocking {
        // GIVEN
        val pendiente = Pendientes(
            idPendientes = "", // Vacío para crear nuevo
            titulo = "Tarea de Prueba",
            descripcion = "Descripción del test",
            fecha = "20/10/2024",
            estado = false, // false = pendiente
            materiaIdMateria = "id_materia_fake"
        )

        // WHEN
        val resultado = repository.guardarPendiente(pendiente)

        // THEN
        assertTrue("El guardado debe retornar Success", resultado.isSuccess)

        // Verificación manual directa en la BD
        val snapshot = db.reference.child("usuarios").child(userId).child("pendientes").get().await()
        assertTrue("Debería haber datos en Firebase", snapshot.exists())

        // Verificamos que el primer hijo tenga el título correcto
        val primerHijo = snapshot.children.first()
        val pendienteGuardado = primerHijo.getValue(Pendientes::class.java)
        assertEquals("Tarea de Prueba", pendienteGuardado?.titulo)
    }

    // --- TEST 2: Obtener Pendientes con Flow (Read) ---
    @Test
    fun testObtenerPendientesFlow() = runBlocking {
        // GIVEN: Guardamos 2 pendientes iniciales
        val p1 = Pendientes(idPendientes = "", titulo = "P1", descripcion = "D1", fecha = "", estado = false, materiaIdMateria = "")
        val p2 = Pendientes(idPendientes = "", titulo = "P2", descripcion = "D2", fecha = "", estado = true, materiaIdMateria = "")

        repository.guardarPendiente(p1)
        repository.guardarPendiente(p2)

        // WHEN: Consumimos el Flow
        // .first() suspende la corrutina hasta que el Flow emita el primer valor (la lista actual)
        val listaEmitida = repository.obtenerPendientes().first()

        // THEN
        assertEquals("La lista debería tener 2 elementos", 2, listaEmitida.size)
        // Verificamos que contenga los títulos
        val titulos = listaEmitida.map { it.titulo }
        assertTrue(titulos.contains("P1"))
        assertTrue(titulos.contains("P2"))
    }

    // --- TEST 3: Eliminar Pendiente (Delete) ---
    @Test
    fun testEliminarPendiente() = runBlocking {
        // GIVEN: Un pendiente guardado
        val pendiente = Pendientes(idPendientes = "", titulo = "A borrar", descripcion = "...", fecha = "", estado = false, materiaIdMateria = "")
        repository.guardarPendiente(pendiente)

        // Obtenemos el ID generado recuperando la lista
        val listaInicial = repository.obtenerPendientes().first()
        val idGenerado = listaInicial[0].idPendientes

        // WHEN: Eliminamos
        repository.eliminarPendiente(idGenerado)

        // THEN: El Flow debe emitir una lista vacía o sin ese elemento
        val listaFinal = repository.obtenerPendientes().first()
        assertTrue("La lista debería estar vacía tras borrar", listaFinal.isEmpty())
    }

    // --- TEST 4: Actualizar Estado (Update) ---
    @Test
    fun testActualizarPendiente() = runBlocking {
        // GIVEN: Un pendiente guardado como 'false' (no completado)
        val pendiente = Pendientes(idPendientes = "", titulo = "Original", estado = false, descripcion = "", fecha = "", materiaIdMateria = "")
        repository.guardarPendiente(pendiente)

        val lista = repository.obtenerPendientes().first()
        val pendienteGuardado = lista[0]

        // WHEN: Modificamos el estado a 'true' y guardamos de nuevo (Update)
        val pendienteActualizado = pendienteGuardado.copy(estado = true)
        repository.guardarPendiente(pendienteActualizado)

        // THEN
        val listaFinal = repository.obtenerPendientes().first()
        assertTrue("El estado debería ser true", listaFinal[0].estado)
        assertEquals("El ID debe ser el mismo", pendienteGuardado.idPendientes, listaFinal[0].idPendientes)
    }
}