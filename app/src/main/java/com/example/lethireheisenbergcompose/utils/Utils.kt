package com.example.lethireheisenbergcompose.utils

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDateTime
import java.util.UUID

fun generateId(): String {
    return UUID.randomUUID().toString()
}

@RequiresApi(Build.VERSION_CODES.O)
fun LocalDateTime.showFormat(): String {
    return "${this.dayOfMonth} ${this.month} ${this.year}"
}
