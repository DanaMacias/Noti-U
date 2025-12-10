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


    suspend fun verificarVencimientoPeriodo() {
        if (userId.isEmpty()) return

        try {

            val snapshot = db.child("users").child(userId).get().await()
            val fechaFinStr = snapshot.child("fechaFin").getValue(String::class.java)

            if (!fechaFinStr.isNullOrEmpty()) {
                val fechaFin = parsearFecha(fechaFinStr)
                val fechaHoy = LocalDate.now()


                if (fechaFin != null && fechaHoy.isAfter(fechaFin)) {
                    // 3. DELETE Materias and Pendientes
                    db.child("usuarios").child(userId).child("materias").removeValue().await()
                    db.child("usuarios").child(userId).child("pendientes").removeValue().await()

                    // Optional: You might want to clear the dates in the profile too so it doesn't run every time
                    // db.child("users").child(userId).child("fechaFin").removeValue()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    // Helper to parse date (handles 01/01/2024 and 1/1/2024)
    private fun parsearFecha(fechaStr: String): LocalDate? {
        return try {
            DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.getDefault()).parse(fechaStr, LocalDate::from)
        } catch (e: Exception) {
            try {
                DateTimeFormatter.ofPattern("d/M/yyyy", Locale.getDefault()).parse(fechaStr, LocalDate::from)
            } catch (e2: Exception) { null }
        }
    }

    // ... (Keep your existing functions: guardarMateria, obtenerMaterias, eliminarMateria, etc.) ...

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

    suspend fun eliminarMateria(materiaId: String): Boolean {
        if (userId.isEmpty()) return false
        return try {
            db.child("usuarios").child(userId).child("materias").child(materiaId).removeValue().await()
            true
        } catch (e: Exception) { false }
    }

    suspend fun obtenerMateriaPorId(id: String): Materia? {
        if (userId.isEmpty()) return null
        return try {
            val snapshot = db.child("usuarios").child(userId).child("materias").child(id).get().await()
            snapshot.getValue(Materia::class.java)
        } catch (e: Exception) { null }
    }
}