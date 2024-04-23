package com.example.lethireheisenbergcompose.data

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.lethireheisenbergcompose.model.DbOperation
import com.example.lethireheisenbergcompose.model.Operation
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await
import com.google.firebase.Timestamp
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class OperationRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
) : OperationRepository {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun saveOperationData(operation: Operation) {
        firestore.collection("operations").add(operation.convertToDbOperation())
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun getWalletOperations(walletId: String) : Flow<List<Operation>> =  flow {
        val operations: MutableList<Operation> = mutableListOf()
        val query = firestore.collection("operations").whereEqualTo("walletId", walletId)
        val documents = query.get().await()
        val document = documents.documents
        for (item in document) {
            item.data?.let {
                val operation = convertDocumentToOperation(it)
                operations.add(operation)
            }
        }
        emit(operations)
    }.flowOn(Dispatchers.IO)

    @RequiresApi(Build.VERSION_CODES.O)
    private fun convertDocumentToOperation(userData: Map<String, Any>): Operation {
        val walletId = userData["walletId"] as String
        val type = userData["type"] as String
        val amount = userData["amount"] as Double
        val timestamp = userData["timestamp"] as Timestamp
        val operation = DbOperation(walletId, type, amount, timestamp)
        return Operation.convertToOperation(operation)
    }
}