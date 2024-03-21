package com.example.lethireheisenbergcompose.data

import com.example.lethireheisenbergcompose.model.Hire

interface HireRepository {
    fun saveHireData(hire: Hire)
}