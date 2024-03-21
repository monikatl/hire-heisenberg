package com.example.lethireheisenbergcompose.network.quotes_api

import com.example.lethireheisenbergcompose.model.Quote
import javax.inject.Inject

class QuotesRepository@Inject constructor(private val service: QuotesService) {
    suspend fun getRandomQuote(): Quote {
        return service.getRandomQuote().first()
    }
}