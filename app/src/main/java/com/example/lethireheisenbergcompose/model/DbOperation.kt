package com.example.lethireheisenbergcompose.model

import com.google.firebase.Timestamp

class DbOperation (
    val walletId: String = "",
    val type: String = "",
    val amount: Double = 0.0,
    val timestamp: Timestamp
) {}
