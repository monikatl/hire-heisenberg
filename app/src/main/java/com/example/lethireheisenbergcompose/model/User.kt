package com.example.lethireheisenbergcompose.model

data class User( val userName: String,
                 val userAge: String,
                 val userOccupation: String) {
    constructor(): this("", "", "")
}