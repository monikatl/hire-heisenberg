package com.example.lethireheisenbergcompose.data

import com.example.lethireheisenbergcompose.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth,
    private val authRepository: AuthRepository
) : UserRepository {

    override fun getUserDetails(): Flow<User?> =  flow {
        var user: User? = null
        val query = firestore.collection("users").whereEqualTo("userId", authRepository.currentUserUid)
        val documents = query.get().await()
        val document = documents.documents.firstOrNull()
        if (document != null && document.exists()) {
            user = document.data?.let { convertDocumentToUser(it) }
        }
        emit(user)
    }.flowOn(Dispatchers.IO)


    private fun convertDocumentToUser(userData: Map<String, Any>): User {
        return User().apply {
            userId = userData["userId"] as String
            name = userData["name"] as String
            email = userData["email"] as String
        }
    }

    override fun saveUserData(userId: String, username: String, email: String) {
        val userData = User(userId, username, email)
        val userRef = firestore.collection("users").add(userData).addOnSuccessListener {  }

    }

    override fun deleteUser() {
        auth.currentUser?.delete()
            ?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Handle successful account deletion
                } else {
                    // Handle account deletion failure
                }
            }
    }

    override fun updateUser(username: String) {
        val userUpdates = mapOf<String, Any>(
            "username" to username
        )

        val userRef = firestore.collection("users").document(auth.currentUser?.uid ?: "")
        userRef.update(userUpdates)
            .addOnSuccessListener {
                // Handle successful user data update
            }
            .addOnFailureListener {
                // Handle user data update failure
            }
    }

}
