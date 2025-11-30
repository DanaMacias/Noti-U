package com.example.noti_u.data.repository


import com.example.noti_u.data.model.User
import com.example.noti_u.utils.FirebaseDataSource
import kotlinx.coroutines.tasks.await

class AuthRepository {


    private val auth = FirebaseDataSource.auth
    private val db = FirebaseDataSource.database


    suspend fun login(email: String, password: String): Result<User> {
        return try {
            val result = auth.signInWithEmailAndPassword(email, password).await()
            val firebaseUser = result.user

            if (firebaseUser != null) {
                val user = User(
                    id = firebaseUser.uid,
                    correo = firebaseUser.email ?: ""
                )
                Result.success(user)
            } else {
                Result.failure(Exception("Usuario nulo"))
            }

        } catch (e: Exception) {
            Result.failure(e)
        }
    }


    suspend fun register(user: User, password: String): Result<Unit> {
        return try {
            val result = auth.createUserWithEmailAndPassword(user.correo, password).await()
            val uid = result.user!!.uid

            val userToSave = user.copy(id = uid)

            db.getReference("users")
                .child(uid)
                .setValue(userToSave)
                .await()

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }


    suspend fun getUserData(uid: String): User? {
        return try {
            val snapshot = db.getReference("users")
                .child(uid)
                .get()
                .await()

            snapshot.getValue(User::class.java)
        } catch (e: Exception) {
            null
        }
    }


    fun logout() {
        auth.signOut()
    }
}
