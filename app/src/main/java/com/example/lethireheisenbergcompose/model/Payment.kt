package com.example.lethireheisenbergcompose.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
open class Payment (val hours: Int, val amount: Double) : Parcelable {
    var hourCounter: Int = hours
    var amountCounter: Double = amount

    class Hourly(hours: Int, amount: Double) : Payment(hours, amount) {

    }

    class Whole(hours: Int, amount: Double) : Payment(hours, amount) {

    }
}