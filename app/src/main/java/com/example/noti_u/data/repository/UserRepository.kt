package com.example.noti_u.data.repository


import com.example.noti_u.data.model.User
import com.example.noti_u.utils.FirebaseDataSource
import kotlinx.coroutines.tasks.await

class UserRepository {

    private val db = FirebaseDataSource.database

    suspend fun saveUser(user: User): Result<Unit> {
        return try {
            db.getReference("users")
                .child(user.id)
                .setValue(user)
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
}
