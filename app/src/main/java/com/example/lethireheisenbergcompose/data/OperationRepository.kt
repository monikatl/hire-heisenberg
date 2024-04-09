package com.example.lethireheisenbergcompose.data

import com.example.lethireheisenbergcompose.model.Operation
import kotlinx.coroutines.flow.Flow

interface OperationRepository {
    fun saveOperationData(operation: Operation)
    fun getWalletOperations(walletId: String) : Flow<List<Operation>>
}