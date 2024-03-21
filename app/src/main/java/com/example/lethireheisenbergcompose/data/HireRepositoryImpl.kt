package com.example.lethireheisenbergcompose.data

import com.example.lethireheisenbergcompose.model.Hire
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject

class HireRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : HireRepository {
    override fun saveHireData(hire: Hire) {
        val hireData = hire.convertToDbHire()
        firestore.collection("hires").add(hireData)

    }
}