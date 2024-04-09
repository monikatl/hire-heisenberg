package com.example.lethireheisenbergcompose.model

import com.example.lethireheisenbergcompose.utils.generateId
import javax.money.CurrencyUnit
import javax.money.Monetary
import javax.money.MonetaryAmount
import javax.money.UnknownCurrencyException


class Wallet {

    var id: String = generateId()
    var currency: Currency = Currency.PLN
    var contents: Double = 0.0

    var lastOperation: Operation? = null

    val history: List<Operation> = emptyList()


    fun deposit(amount: Double): Wallet {
        contents += amount
        lastOperation = addOperationToHistory(amount, OperationType.DEPOSIT)
        return this
    }

    fun draw(amount: Double): Wallet {
        contents -= amount
        lastOperation = addOperationToHistory(amount, OperationType.DRAW)
        return this
    }

    private fun addOperationToHistory(amount: Double, type: OperationType): Operation? {
        val operation = Operation(id, type, amount)
        return if (history.toMutableList().add(operation))
            operation
        else
            null

    }

    private fun createMonetaryAmount(amount: Long, currency: CurrencyUnit): MonetaryAmount {
        return Monetary.getDefaultAmountFactory().setCurrency(currency).setNumber(amount).create()
    }

    fun convertToDbWallet(): DbWallet {
        return DbWallet(id, currency.name, contents)
    }

    companion object {
        const val PLN = "PLN"
        const val USD = "USD"
        const val EUR = "EUR"

        private fun getCurrency(currencyCode: String): CurrencyUnit {
            try {
                return Monetary.getCurrency(currencyCode)
            } catch (e: UnknownCurrencyException) {
                e.printStackTrace()
                throw IllegalArgumentException("Invalid currency code: $currencyCode")
            }
        }

        fun convertToWallet(dbWallet: DbWallet): Wallet {
            return Wallet().apply {
                id = dbWallet.id
                currency = Currency.valueOf(dbWallet.currency)
                contents = dbWallet.contents
            }
        }
    }
}
