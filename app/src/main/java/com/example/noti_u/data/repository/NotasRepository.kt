package com.example.noti_u.data.repository

import com.example.noti_u.data.model.Notas
import com.example.noti_u.utils.FirebaseDataSource
import kotlinx.coroutines.tasks.await

class NotasRepository {

    private val db = FirebaseDataSource.database.reference.child("notas")

    // Crear o actualizar nota
    suspend fun guardarNota(userId: String, nota: Notas) {
        val key = if (nota.id.isEmpty()) {
            db.child(userId).push().key ?: throw Exception("No se pudo generar ID")
        } else {
            nota.id
        }
        val notaConId = nota.copy(id = key)
        db.child(userId).child(key).setValue(notaConId).await()
    }

    // Eliminar nota
    suspend fun eliminarNota(userId: String, notaId: String) {
        db.child(userId).child(notaId).removeValue().await()
    }

    // Leer notas (de forma reactiva con listener)
    fun getNotasListener(userId: String, onChange: (List<Notas>) -> Unit) {
        db.child(userId).addValueEventListener(object : com.google.firebase.database.ValueEventListener {
            override fun onDataChange(snapshot: com.google.firebase.database.DataSnapshot) {
                val list = mutableListOf<Notas>()
                for (child in snapshot.children) {
                    val nota = child.getValue(Notas::class.java)
                    if (nota != null) list.add(nota)
                }
                onChange(list)
            }

            override fun onCancelled(error: com.google.firebase.database.DatabaseError) {}
        })
    }

    // NUEVO: Consultar una nota específica
    suspend fun consultarNota(userId: String, notaId: String): Notas? {
        val snapshot = db.child(userId).child(notaId).get().await()
        return snapshot.getValue(Notas::class.java)
    }

    // NUEVO: Editar nota (es lo mismo que guardar, pero más explícito)
    suspend fun editarNota(userId: String, nota: Notas) {
        if (nota.id.isEmpty()) throw Exception("La nota debe tener un ID para editarse")
        db.child(userId).child(nota.id).setValue(nota).await()
    }
}