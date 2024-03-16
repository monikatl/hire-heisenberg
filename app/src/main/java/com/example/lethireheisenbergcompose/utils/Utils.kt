package com.example.lethireheisenbergcompose.utils

import android.text.BoringLayout
import java.util.UUID

fun generateUserId(): String {
    return UUID.randomUUID().toString()
}
