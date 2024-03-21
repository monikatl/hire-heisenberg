package com.example.lethireheisenbergcompose.network.quotes_api

import com.example.lethireheisenbergcompose.model.Quote
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface QuotesApi {
    companion object {
        const val BASE_URL = "https://api.breakingbadquotes.xyz/v1/"
        const val RANDOM_QUOTE_ENDPOINT = "quotes"
    }

    @GET("quotes")
    suspend fun getRandomQuote(): Response<List<Quote>>

    @GET("$RANDOM_QUOTE_ENDPOINT/{count}")
    suspend fun getSeveralRandomQuotes(@Path("count") quotesCount: Int): Response<List<Quote>>

    //https://breakingbadapi.com/api/quote?author=Jesse+Pinkman
}