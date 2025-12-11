package com.example.noti_u.data.repository

import com.example.noti_u.data.model.Materia
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.tasks.await
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

class MateriaRepository {

    private val db = FirebaseDatabase.getInstance().reference
    private val auth = FirebaseAuth.getInstance()

    private val userId: String
        get() = auth.currentUser?.uid ?: ""

    // --- 1. Borrado por Vencimiento de Periodo ---
    suspend fun verificarVencimientoPeriodo() {
        if (userId.isEmpty()) return

        try {
            // Nota: Verifica si tu nodo de perfil es "users" o "usuarios".
            // En tu código usas ambos ("users" para leer fecha, "usuarios" para borrar datos).
            val snapshot = db.child("users").child(userId).get().await()

            // Asumimos que el campo se llama "fechaFin" dentro del usuario
            val fechaFinStr = snapshot.child("fechaFin").getValue(String::class.java)

            if (!fechaFinStr.isNullOrEmpty()) {
                val fechaFin = parsearFecha(fechaFinStr)
                val fechaHoy = LocalDate.now()

                if (fechaFin != null && fechaHoy.isAfter(fechaFin)) {
                    // AQUÍ YA ESTABA BIEN:
                    // Al vencer el periodo, borramos TODAS las materias y TODOS los pendientes.
                    db.child("usuarios").child(userId).child("materias").removeValue().await()
                    db.child("usuarios").child(userId).child("pendientes").removeValue().await()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    // --- 2. Borrado Manual (ACTUALIZADO) ---
    suspend fun eliminarMateria(materiaId: String): Boolean {
        if (userId.isEmpty()) return false
        return try {
            val userRef = db.child("usuarios").child(userId)

            // PASO A: Buscar y borrar los pendientes asociados a esta materia
            val pendientesRef = userRef.child("pendientes")

            // Buscamos pendientes donde 'materiaIdMateria' sea igual al ID que vamos a borrar
            val query = pendientesRef.orderByChild("materiaIdMateria").equalTo(materiaId)
            val snapshot = query.get().await()

            // Recorremos los resultados y los borramos uno por uno
            for (pendienteSnapshot in snapshot.children) {
                pendienteSnapshot.ref.removeValue().await()
            }

            // PASO B: Ahora sí, borramos la materia
            userRef.child("materias").child(materiaId).removeValue().await()

            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    // --- Helpers y otros métodos ---

    private fun parsearFecha(fechaStr: String): LocalDate? {
        return try {
            DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.getDefault()).parse(fechaStr, LocalDate::from)
        } catch (e: Exception) {
            try {
                DateTimeFormatter.ofPattern("d/M/yyyy", Locale.getDefault()).parse(fechaStr, LocalDate::from)
            } catch (e2: Exception) { null }
        }
    }

    suspend fun obtenerMaterias(): List<Materia> {
        if (userId.isEmpty()) return emptyList()
        return try {
            val snapshot = db.child("usuarios").child(userId).child("materias").get().await()
            snapshot.children.mapNotNull { it.getValue(Materia::class.java) }
        } catch (e: Exception) { emptyList() }
    }

    suspend fun guardarMateria(materia: Materia): Result<Unit> {
        if (userId.isEmpty()) return Result.failure(Exception("No user"))
        return try {
            val ref = db.child("usuarios").child(userId).child("materias")
            val key = if (materia.id.isNotEmpty()) materia.id else ref.push().key ?: return Result.failure(Exception("Error key"))
            ref.child(key).setValue(materia.copy(id = key)).await()
            Result.success(Unit)
        } catch (e: Exception) { Result.failure(e) }
    }

    suspend fun obtenerMateriaPorId(id: String): Materia? {
        if (userId.isEmpty()) return null
        return try {
            val snapshot = db.child("usuarios").child(userId).child("materias").child(id).get().await()
            snapshot.getValue(Materia::class.java)
        } catch (e: Exception) { null }
    }
}