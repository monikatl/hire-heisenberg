package com.example.lethireheisenbergcompose.model

import android.os.Build
import androidx.annotation.RequiresApi
import com.google.firebase.Timestamp
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.util.Date


enum class OperationType {
    DEPOSIT, DRAW
}


class Operation (
    val id: String,
    val type: OperationType,
    val amount: Double
) {

    @RequiresApi(Build.VERSION_CODES.O)
    var date: LocalDateTime = LocalDateTime.now()

    @RequiresApi(Build.VERSION_CODES.O)
    fun convertToDbOperation(): DbOperation {
        return DbOperation(
            id,
            type.name,
            amount,
            Timestamp(Date.from(date.toInstant(ZoneOffset.UTC)))
        )
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun timestampToLocalDateTime(timestamp: Timestamp): LocalDateTime {
        val seconds = timestamp.seconds
        val nanoseconds = timestamp.nanoseconds
        return LocalDateTime.ofEpochSecond(seconds, nanoseconds, ZoneOffset.UTC)
    }

    companion object {
        @RequiresApi(Build.VERSION_CODES.O)
        fun convertToOperation(dbOperation: DbOperation): Operation {
            return Operation(
                dbOperation.walletId,
                OperationType.valueOf(dbOperation.type),
                dbOperation.amount
            ).apply {
                this.date = timestampToLocalDateTime(dbOperation.timestamp)
            }
        }
    }


}


