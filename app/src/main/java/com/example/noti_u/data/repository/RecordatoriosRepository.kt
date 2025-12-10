package com.example.noti_u.data.repository

import com.example.noti_u.data.model.Recordatorios
import com.example.noti_u.utils.FirebaseDataSource
import kotlinx.coroutines.tasks.await

class RecordatoriosRepository {

    private val db = FirebaseDataSource.database.reference.child("recordatorios")


    suspend fun guardarRecordatorio(userId: String, recordatorio: Recordatorios) {
        val key = if (recordatorio.id.isEmpty()) {
            db.child(userId).push().key ?: throw Exception("No se pudo generar ID")
        } else {
            recordatorio.id
        }
        val recordatorioConId = recordatorio.copy(id = key)
        db.child(userId).child(key).setValue(recordatorioConId).await()
    }


    suspend fun eliminarRecordatorio(userId: String, recordatorioId: String) {
        db.child(userId).child(recordatorioId).removeValue().await()
    }


    fun getRecordatoriosListener(userId: String, onChange: (List<Recordatorios>) -> Unit) {
        db.child(userId).addValueEventListener(object : com.google.firebase.database.ValueEventListener {
            override fun onDataChange(snapshot: com.google.firebase.database.DataSnapshot) {
                val list = mutableListOf<Recordatorios>()
                for (child in snapshot.children) {
                    val recordatorio = child.getValue(Recordatorios::class.java)
                    if (recordatorio != null) list.add(recordatorio)
                }
                onChange(list)
            }

            override fun onCancelled(error: com.google.firebase.database.DatabaseError) {}
        })
    }


    suspend fun consultarRecordatorio(userId: String, recordatorioId: String): Recordatorios? {
        val snapshot = db.child(userId).child(recordatorioId).get().await()
        return snapshot.getValue(Recordatorios::class.java)
    }


    suspend fun editarRecordatorio(userId: String, recordatorio: Recordatorios) {
        if (recordatorio.id.isEmpty()) throw Exception("El recordatorio debe tener un ID para editarse")
        db.child(userId).child(recordatorio.id).setValue(recordatorio).await()
    }
}