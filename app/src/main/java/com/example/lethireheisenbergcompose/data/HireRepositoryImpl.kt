package com.example.lethireheisenbergcompose.data

import com.example.lethireheisenbergcompose.model.DbHire
import com.example.lethireheisenbergcompose.model.DbUser
import com.example.lethireheisenbergcompose.model.Hire
import com.example.lethireheisenbergcompose.model.Service
import com.example.lethireheisenbergcompose.model.ServiceProvider
import com.example.lethireheisenbergcompose.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class HireRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val authRepository: AuthRepository
) : HireRepository {
    override fun saveHireData(hire: Hire) {
        val hireData = hire.convertToDbHire()
        firestore.collection("hires").add(hireData)
    }

    override suspend fun updateHireData(hireId: String, changedField: String, newValue: String) {
        val query = firestore.collection("hires").whereEqualTo("id", hireId)
        val document = query.get().await().first()
        val hireRef = firestore.collection("hires").document(document.id)
        hireRef.update(changedField, newValue).await()

    }

    override fun getUserHires() : Flow<List<Hire>> =  flow {
        val hires: MutableList<Hire> = mutableListOf()
        val query = firestore.collection("hires").whereEqualTo("user", authRepository.currentUserUid)
        val documents = query.get().await()
        val document = documents.documents
        for (item in document) {
            item.data?.let {
                val hire = convertDocumentToHire(it)
                hires.add(hire)
            }
        }
        emit(hires)
    }.flowOn(Dispatchers.IO)

    private fun convertDocumentToHire(userData: Map<String, Any>): Hire {
        val dbHire = DbHire().apply {
            id = userData["id"] as String
            name = userData["name"] as String
            service_provider = userData["service_provider"] as String
            user = userData["user"] as String
            counter = (userData["counter"] as Long).toInt()
            amount = userData["amount"] as Double
            status = userData["status"] as String
        }
        val serviceProvider = ServiceProvider(dbHire.service_provider)
        return Hire.convertToHire(dbHire, serviceProvider)
    }
}