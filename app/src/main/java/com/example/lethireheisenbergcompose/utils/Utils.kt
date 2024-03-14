package com.example.lethireheisenbergcompose.utils

import java.util.UUID

fun generateUserId(): String {
    return UUID.randomUUID().toString()
}