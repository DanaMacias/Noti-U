package com.example.noti_u.ui.viewmodel

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.noti_u.data.model.Materia
import com.example.noti_u.data.repository.MateriaRepository
import com.example.noti_u.data.repository.PendientesRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AgregarPendienteViewModelInstrumentedTest {

    private lateinit var viewModel: AgregarPendienteViewModel
    private lateinit var auth: FirebaseAuth
    private lateinit var materiaRepo: MateriaRepository
    private lateinit var pendientesRepo: PendientesRepository

    @Before
    fun setUp() = runBlocking {
        // 1. Inicializar Firebase y Repositorios
        auth = FirebaseAuth.getInstance()
        materiaRepo = MateriaRepository()
        pendientesRepo = PendientesRepository()

        // 2. Iniciar sesión anónima (CRUCIAL para que Firebase acepte las peticiones)
        if (auth.currentUser == null) {
            auth.signInAnonymously().await()
        }

        // 3. Inicializar el ViewModel
        // IMPORTANTE: Los ViewModels usan viewModelScope (Main Thread).
        // En tests instrumentados, a veces es necesario forzar la inicialización en el hilo principal
        withContext(Dispatchers.Main) {
            viewModel = AgregarPendienteViewModel()
        }

        // Limpiamos datos de prueba anteriores
        limpiarDatos()
    }

    @After
    fun tearDown() = runBlocking {
        limpiarDatos()
        // No cerramos sesión (signOut) aquí para no afectar otros tests si se corren en conjunto,
        // o puedes descomentarlo si prefieres limpieza total.
        // auth.signOut()
    }

    private suspend fun limpiarDatos() {
        val uid = auth.currentUser?.uid ?: return
        // Borramos pendientes y materias del usuario de prueba
        val dbRef = com.google.firebase.database.FirebaseDatabase.getInstance().reference
        dbRef.child("pendientes").child(uid).removeValue().await()
        dbRef.child("materias").child(uid).removeValue().await()
    }

    // --- TEST 1: Verificar Cargar Materias (init) ---
    @Test
    fun testCargarMaterias_ActualizaStateFlow() = runBlocking {
        // GIVEN: Insertamos una materia directamente en el repo para que exista algo que cargar
        val materiaPrueba = Materia(id = "", nombre = "Materia Test", dias = emptyMap(), horaInicio = emptyMap(), horaFin = emptyMap(), color = "", salon = "", enlace = "")
        materiaRepo.guardarMateria(materiaPrueba)

        // WHEN: Reinicializamos el ViewModel para que se dispare el 'init { cargarMaterias() }'
        // y detecte la materia recién guardada.
        withContext(Dispatchers.Main) {
            viewModel = AgregarPendienteViewModel()
        }

        // Esperamos un poco a que Firebase responda y el StateFlow se actualice
        delay(1500)

        // THEN: La lista de materias en el ViewModel no debe estar vacía
        val listaMaterias = viewModel.materias.value
        assertTrue("La lista de materias debería tener elementos", listaMaterias.isNotEmpty())
        assertEquals("El nombre de la materia debe coincidir", "Materia Test", listaMaterias[0].nombre)
    }

    // --- TEST 2: Verificar Guardar Pendiente ---
    @Test
    fun testGuardarPendiente_CambiaEstadoAExitoso() = runBlocking {
        // GIVEN: El estado inicial es false
        assertFalse(viewModel.guardadoExitoso.value)

        // WHEN: Llamamos al método guardar con la nueva firma (incluyendo fecha)
        val titulo = "Tarea Unit Test"
        val desc = "Descripción de prueba"
        val materiaId = "12345"
        val fecha = "25/12/2025" // Fecha manual

        viewModel.guardar(titulo, desc, materiaId, fecha)

        // Esperamos a que la corrutina termine y Firebase responda
        delay(1500)

        // THEN: El estado guardadoExitoso debe cambiar a true
        assertTrue("El estado guardadoExitoso debería ser true tras guardar", viewModel.guardadoExitoso.value)

        // Verificación extra (Opcional): Consultar repo para ver si es verdad
        val pendientes = pendientesRepo.obtenerPendientes()

    }

    // --- TEST 3: Verificar Reiniciar Estado ---
    @Test
    fun testReiniciarEstado_VuelveAFalse() = runBlocking {
        // GIVEN: Forzamos el estado a true (simulando un guardado exitoso previo)
        viewModel.guardar("T", "D", "ID", "F")
        delay(1000)
        assertTrue(viewModel.guardadoExitoso.value)

        // WHEN: Llamamos a reiniciarEstado
        viewModel.reiniciarEstado()

        // THEN: El estado debe volver inmediatamente a false
        assertFalse("El estado debería volver a false", viewModel.guardadoExitoso.value)
    }
}