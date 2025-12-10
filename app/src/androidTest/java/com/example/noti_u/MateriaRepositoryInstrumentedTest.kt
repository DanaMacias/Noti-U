package com.example.noti_u.data.repository

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.noti_u.data.model.Materia
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@RunWith(AndroidJUnit4::class)
class MateriaRepositoryInstrumentedTest {

    private lateinit var repository: MateriaRepository
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseDatabase

    @Before
    fun setUp() = runBlocking {
        // 1. Inicializar instancias
        auth = FirebaseAuth.getInstance()
        db = FirebaseDatabase.getInstance()
        repository = MateriaRepository()

        // 2. Iniciar sesión anónima para tener un userId válido
        // (El repositorio retorna listas vacías si userId es nulo/vacío)
        auth.signInAnonymously().await()

        // 3. Limpiar la base de datos para este usuario de prueba antes de empezar
        limpiarDatosUsuario()
    }

    @After
    fun tearDown() = runBlocking {
        // Limpieza posterior (Opcional, pero recomendada)
        limpiarDatosUsuario()
        auth.signOut()
    }

    // Helper para borrar datos del usuario actual
    private suspend fun limpiarDatosUsuario() {
        val uid = auth.currentUser?.uid ?: return
        db.reference.child("usuarios").child(uid).removeValue().await()
        db.reference.child("users").child(uid).removeValue().await()
    }

    // --- TEST 1: Verificar Guardado y Lectura ---
    @Test
    fun testGuardarYObtenerMateria() = runBlocking {
        // GIVEN: Una materia nueva
        val materia = Materia(
            id = "", // ID vacío para que genere uno nuevo
            nombre = "Matemáticas Test",
            dias = mapOf("Lunes" to true),
            horaInicio = mapOf("Lunes" to "08:00"),
            horaFin = mapOf("Lunes" to "10:00"),
            color = "#FF0000",
            salon = "A-101",
            enlace = ""
        )

        // WHEN: Guardamos la materia
        val resultado = repository.guardarMateria(materia)

        // THEN: El resultado es exitoso y la lista contiene la materia
        assertTrue("El guardado debería ser exitoso", resultado.isSuccess)

        val listaMaterias = repository.obtenerMaterias()
        assertTrue("La lista no debería estar vacía", listaMaterias.isNotEmpty())
        assertEquals("El nombre debe coincidir", "Matemáticas Test", listaMaterias[0].nombre)
    }

    // --- TEST 2: Verificar Eliminación ---
    @Test
    fun testEliminarMateria() = runBlocking {
        // GIVEN: Una materia ya guardada
        val materia = Materia(id = "", nombre = "Física", dias = emptyMap(), horaInicio = emptyMap(), horaFin = emptyMap(), color = "", salon = "", enlace = "")
        repository.guardarMateria(materia)

        // Recuperamos la materia guardada para obtener su ID generado por Firebase
        val listaInicial = repository.obtenerMaterias()
        val idGenerado = listaInicial[0].id

        // WHEN: Eliminamos
        val exito = repository.eliminarMateria(idGenerado)

        // THEN: La lista debe estar vacía
        assertTrue("La eliminación retorna true", exito)
        val listaFinal = repository.obtenerMaterias()
        assertTrue("La lista debería estar vacía después de borrar", listaFinal.isEmpty())
    }

    // --- TEST 3: Vencimiento del Periodo (CASO: Fecha Vencida) ---
    @Test
    fun testVerificarVencimiento_BorraDatosSiFechaPaso() = runBlocking {
        // GIVEN:
        // 1. Una materia guardada
        val materia = Materia(id = "", nombre = "Materia Antigua", dias = emptyMap(), horaInicio = emptyMap(), horaFin = emptyMap(), color = "", salon = "", enlace = "")
        repository.guardarMateria(materia)

        // 2. Simulamos que en el perfil del usuario hay una fecha antigua (Ej: año 2000)
        val uid = auth.currentUser!!.uid
        db.reference.child("users").child(uid).child("fechaFin").setValue("01/01/2000").await()

        // WHEN: Ejecutamos la verificación
        repository.verificarVencimientoPeriodo()

        // THEN: La materia debió ser borrada automáticamente
        val lista = repository.obtenerMaterias()
        assertTrue("La lista debería estar vacía porque el periodo venció", lista.isEmpty())
    }

    // --- TEST 4: Vencimiento del Periodo (CASO: Fecha Futura) ---
    @Test
    fun testVerificarVencimiento_NoBorraSiFechaEsFutura() = runBlocking {
        // GIVEN:
        // 1. Una materia guardada
        val materia = Materia(id = "", nombre = "Materia Futura", dias = emptyMap(), horaInicio = emptyMap(), horaFin = emptyMap(), color = "", salon = "", enlace = "")
        repository.guardarMateria(materia)

        // 2. Simulamos una fecha futura (Año actual + 1)
        val anioFuturo = LocalDate.now().year + 1
        val fechaFuturaStr = "01/01/$anioFuturo"

        val uid = auth.currentUser!!.uid
        db.reference.child("users").child(uid).child("fechaFin").setValue(fechaFuturaStr).await()

        // WHEN: Ejecutamos la verificación
        repository.verificarVencimientoPeriodo()

        // THEN: La materia NO debe borrarse
        val lista = repository.obtenerMaterias()
        assertTrue("La lista NO debería estar vacía", lista.isNotEmpty())
        assertEquals("Materia Futura", lista[0].nombre)
    }
}