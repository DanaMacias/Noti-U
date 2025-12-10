package com.example.noti_u.ui.viewmodel

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.noti_u.data.model.Notas
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.UUID

@RunWith(AndroidJUnit4::class)
class NotasViewModelInstrumentedTest {

    private lateinit var viewModel: NotasViewModel
    private lateinit var auth: FirebaseAuth
    private lateinit var userId: String

    @Before
    fun setUp() = runBlocking {
        // 1. Instancias de Firebase
        auth = FirebaseAuth.getInstance()

        // 2. Login Anónimo (Necesario para escribir en DB)
        if (auth.currentUser == null) {
            auth.signInAnonymously().await()
        }
        userId = auth.currentUser!!.uid

        // 3. Inicializar ViewModel en el Hilo Principal
        withContext(Dispatchers.Main) {
            viewModel = NotasViewModel()
        }

        // 4. Limpiar datos previos
        limpiarDatos()
    }

    @After
    fun tearDown() = runBlocking {
        limpiarDatos()
        // auth.signOut() // Opcional
    }

    private suspend fun limpiarDatos() {
        val dbRef = FirebaseDatabase.getInstance().reference
        dbRef.child("notas").child(userId).removeValue().await()
    }

    // --- TEST 1: Agregar Nota y verificar que 'cargarNotas' actualice la lista ---
    @Test
    fun testAgregarYCargarNotas_ActualizaLista() = runBlocking {
        // GIVEN: Iniciamos la escucha de notas
        withContext(Dispatchers.Main) {
            viewModel.cargarNotas(userId)
        }

        // Assert inicial: lista vacía
        assertTrue(viewModel.notas.isEmpty())

        // WHEN: Agregamos una nota
        var callbackLlamado = false
        withContext(Dispatchers.Main) {
            viewModel.agregarNota("Titulo Test", "Descripcion Test", userId) {
                callbackLlamado = true
            }
        }

        // Esperamos a que Firebase guarde y el listener (cargarNotas) reciba el dato de vuelta
        delay(2000)

        // THEN:
        assertTrue("El callback onSuccess debería haberse ejecutado", callbackLlamado)
        assertTrue("La lista local 'notas' debería tener 1 elemento", viewModel.notas.isNotEmpty())
        assertEquals("El título debe coincidir", "Titulo Test", viewModel.notas[0].titulo)
    }

    // --- TEST 2: Editar Nota ---
    @Test
    fun testEditarNota_ActualizaContenido() = runBlocking {
        // GIVEN: Creamos una nota primero
        // Usamos un ID manual o generamos uno para poder referenciarlo
        val notaId = "id_nota_editar"
        val notaInicial = Notas(id = notaId, titulo = "Viejo", descripcion = "Vieja Desc", userId = userId)

        // Inyectamos directamente en la BD para preparar el escenario
        val dbRef = FirebaseDatabase.getInstance().reference
        dbRef.child("notas").child(userId).child(notaId).setValue(notaInicial).await()

        // Iniciamos el VM y cargamos
        withContext(Dispatchers.Main) {
            viewModel.cargarNotas(userId)
        }
        delay(1000) // Esperar carga

        // WHEN: Editamos la nota usando el ViewModel
        withContext(Dispatchers.Main) {
            viewModel.editarNota(notaId, "Nuevo Titulo", "Nueva Desc", userId) {}
        }

        delay(1000) // Esperar actualización

        // THEN: Verificamos en la lista del ViewModel
        val notaEditada = viewModel.notas.find { it.id == notaId }
        assertNotNull("La nota debería existir en la lista", notaEditada)
        assertEquals("Nuevo Titulo", notaEditada?.titulo)
        assertEquals("Nueva Desc", notaEditada?.descripcion)
    }

    // --- TEST 3: Consultar Nota Individual (Suspend Function) ---
    @Test
    fun testConsultarNota_DevuelveObjetoCorrecto() = runBlocking {
        // GIVEN: Una nota existente
        val notaId = "id_consulta"
        val nota = Notas(id = notaId, titulo = "Busca esto", descripcion = "...", userId = userId)
        FirebaseDatabase.getInstance().reference
            .child("notas").child(userId).child(notaId).setValue(nota).await()

        // WHEN: Llamamos a consultarNota (es suspend, no necesita Dispatchers.Main obligatorio, pero el VM sí)
        // Como consultarNota retorna valor, podemos llamarlo directamente en el runBlocking o withContext
        val resultado = viewModel.consultarNota(userId, notaId)

        // THEN
        assertNotNull("Debería retornar una nota", resultado)
        assertEquals("Busca esto", resultado?.titulo)
    }

    // --- TEST 4: Eliminar Nota ---
    @Test
    fun testEliminarNota_QuitaElementoDeLista() = runBlocking {
        // GIVEN: Agregamos una nota y cargamos la lista
        val notaId = "id_borrar"
        val nota = Notas(id = notaId, titulo = "A borrar", descripcion = "...", userId = userId)
        FirebaseDatabase.getInstance().reference
            .child("notas").child(userId).child(notaId).setValue(nota).await()

        withContext(Dispatchers.Main) {
            viewModel.cargarNotas(userId)
        }
        delay(1000)
        assertEquals(1, viewModel.notas.size)

        // WHEN: Eliminamos
        var eliminadoExito = false
        withContext(Dispatchers.Main) {
            viewModel.eliminarNota(userId, notaId) {
                eliminadoExito = true
            }
        }
        delay(1000)

        // THEN
        assertTrue("Callback onSuccess ejecutado", eliminadoExito)
        assertTrue("La lista debería estar vacía", viewModel.notas.isEmpty())
    }
}