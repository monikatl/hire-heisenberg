package com.example.lethireheisenbergcompose.data

import com.example.lethireheisenbergcompose.model.Hire
import kotlinx.coroutines.flow.Flow

interface HireRepository {
    fun saveHireData(hire: Hire)
    suspend fun updateHireData(hireId: String, changedField: String, newValue: String)

    fun getUserHires(): Flow<List<Hire>?>
}