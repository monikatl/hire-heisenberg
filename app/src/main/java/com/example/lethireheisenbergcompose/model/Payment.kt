package com.example.lethireheisenbergcompose.model

sealed class Payment (val hours: Int, val amount: Double) {
    var hourCounter: Int = hours
    var amountCounter: Double = amount
}

class Hourly(hours: Int, amount: Double) : Payment(hours, amount) {

}
class Whole(hours: Int, amount: Double) : Payment(hours, amount) {

}