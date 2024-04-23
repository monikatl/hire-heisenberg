package com.example.lethireheisenbergcompose.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
open class Payment (val hours: Int, val amount: Double, val type: Pay) : Parcelable {

}