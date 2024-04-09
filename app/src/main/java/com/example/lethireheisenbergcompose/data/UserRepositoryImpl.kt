package com.example.lethireheisenbergcompose.data

import android.app.Application
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import com.example.lethireheisenbergcompose.MainViewModel
import com.example.lethireheisenbergcompose.model.DbUser
import com.example.lethireheisenbergcompose.model.DbWallet
import com.example.lethireheisenbergcompose.model.User
import com.example.lethireheisenbergcompose.model.Wallet
import com.example.lethireheisenbergcompose.ui.home.HomeViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
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

    override fun getWalletDetails(userId: String): Flow<Wallet?> =  flow {
        var wallet: Wallet? = null
        val query = firestore.collection("users").whereEqualTo("userId", userId)
        val documents = query.get().await()
        val document = documents.documents.firstOrNull()
        if (document != null && document.exists()) {
            wallet = document.data?.let { convertDocumentToUser(it) }?.wallet
        }
        emit(wallet)
    }.flowOn(Dispatchers.IO)


    private fun convertDocumentToUser(userData: Map<String, Any>): User {
        val user = DbUser().apply {
            userId = userData["userId"] as String
            name = userData["name"] as String
            email = userData["email"] as String
            wallet = convertDocumentToWallet(userData["wallet"] as Map<String, Any>)
        }
        return User.convertToUser(user)
    }

    private fun convertDocumentToWallet(walletData: Map<String, Any>): DbWallet {
        return DbWallet().apply {
            id = walletData["id"] as String
            currency = walletData["currency"] as String
            contents = walletData["contents"] as Double
        }
    }

    override fun saveUserData(userId: String, username: String, email: String) {
        val userData = User().apply {
            this.userId = userId
            this.name = name
            this.email = email
        }.convertToDbUser()
        firestore.collection("users").add(userData)
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

    override fun updateUser(updatedData: Map<String, String>) {
        val userRef = firestore.collection("users").document(auth.currentUser?.uid ?: "")
        userRef.update(updatedData)
            .addOnSuccessListener {
                // Handle successful user data update
            }
            .addOnFailureListener {
                // Handle user data update failure
            }
    }

    override fun updateUserWallet(userId: String, updatedWallet: Wallet) {
        firestore.collection("users")
            .whereEqualTo("userId", userId)
            .get()
            .addOnSuccessListener { documents ->
                for (doc in documents) {
                    val docId = doc.id
                    val ref = firestore.collection("users").document(docId)
                    ref.update("wallet", updatedWallet.convertToDbWallet())
                        .addOnSuccessListener {
                            println("Obiekt wallet został zaktualizowany.")
                        }
                        .addOnFailureListener { e ->
                            println("Błąd podczas aktualizacji obiektu wallet: $e")
                        }

                }
            }
    }
}
