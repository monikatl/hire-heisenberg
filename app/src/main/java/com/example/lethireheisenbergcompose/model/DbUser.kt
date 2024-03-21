package com.example.lethireheisenbergcompose.model

data class DbUser(
    var userId: String = "",
    var name: String = "",
    var email: String = "",
    var wallet: DbWallet? = null
)