package com.example.lethireheisenbergcompose.network.quotes_api

import com.example.lethireheisenbergcompose.model.Quote
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class QuotesService @Inject constructor(private val api: QuotesApi){
    suspend fun getRandomQuote(): List<Quote> {
        return withContext(Dispatchers.IO) {
            val quote = api.getRandomQuote()
            quote.body() ?:  emptyList()
        }
    }
}