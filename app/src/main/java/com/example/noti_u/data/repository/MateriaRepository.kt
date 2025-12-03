package com.example.noti_u.data.repository

import com.example.noti_u.data.model.Materia
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.tasks.await

class MateriaRepository {

    private val db = FirebaseDatabase.getInstance().reference
    private val userId = FirebaseAuth.getInstance().currentUser?.uid ?: "null"

    suspend fun guardarMateria(materia: Materia): Result<Unit> {
        return try {
            val materiaRef = db.child("usuarios")
                .child(userId)
                .child("materias")
                .push()

            val materiaConId = materia.copy(id = materiaRef.key ?: "")

            materiaRef.setValue(materiaConId).await()

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun obtenerMaterias(): List<Materia> {
        return try {
            val snapshot = db.child("usuarios")
                .child(userId)
                .child("materias")
                .get()
                .await()

            snapshot.children.mapNotNull { it.getValue(Materia::class.java) }
        } catch (e: Exception) {
            emptyList()
        }
    }
}
