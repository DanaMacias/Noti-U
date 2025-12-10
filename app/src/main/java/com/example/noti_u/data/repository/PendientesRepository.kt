package com.example.noti_u.data.repository

import com.example.noti_u.data.model.Pendientes
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.tasks.await
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class PendientesRepository {

    private val db = FirebaseDatabase.getInstance().reference
    private val userId = FirebaseAuth.getInstance().currentUser?.uid


    suspend fun guardarPendiente(pendiente: Pendientes): Result<Boolean> {
        return try {
            userId?.let { uid ->

                val ref = db.child("usuarios").child(uid).child("pendientes")

                val key = pendiente.idPendientes.ifEmpty {
                    ref.push().key ?: return Result.failure(Exception("No key"))
                }

                val pendienteFinal = pendiente.copy(idPendientes = key)
                ref.child(key).setValue(pendienteFinal).await()
                Result.success(true)
            } ?: Result.failure(Exception("No user"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }


    fun obtenerPendientes(): Flow<List<Pendientes>> = callbackFlow {
        val uid = userId
        if (uid == null) {
            close(Exception("No user"))
            return@callbackFlow
        }

        val ref = db.child("usuarios").child(uid).child("pendientes")

        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val lista = snapshot.children.mapNotNull { it.getValue(Pendientes::class.java) }
                trySend(lista)
            }

            override fun onCancelled(error: DatabaseError) {
                close(error.toException())
            }
        }

        ref.addValueEventListener(listener)
        awaitClose { ref.removeEventListener(listener) }
    }


    suspend fun eliminarPendiente(id: String) {
        userId?.let { uid ->
            db.child("usuarios").child(uid).child("pendientes").child(id).removeValue().await()
        }
    }
}