package com.example.noti_u.data.repository


import com.example.noti_u.data.model.User
import com.example.noti_u.utils.FirebaseDataSource
import kotlinx.coroutines.tasks.await

class AuthRepository {

    private val auth = FirebaseDataSource.auth

    suspend fun login(email: String, password: String): Result<User> {
        return try {
            val result = auth.signInWithEmailAndPassword(email, password).await()
            val firebaseUser = result.user

            if (firebaseUser != null) {
                Result.success(
                    User(
                        id = firebaseUser.uid,
                        correo = firebaseUser.email ?: ""
                    )
                )
            } else {
                Result.failure(Exception("Usuario nulo"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun register(email: String, password: String): Result<String> {
        return try {
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            val uid = result.user!!.uid
            Result.success(uid)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun logout() = auth.signOut()
}
