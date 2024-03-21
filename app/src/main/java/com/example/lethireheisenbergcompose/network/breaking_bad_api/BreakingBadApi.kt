package com.example.lethireheisenbergcompose.network.breaking_bad_api

import com.example.lethireheisenbergcompose.model.Character
import retrofit2.Response
import retrofit2.http.GET

interface BreakingBadApi {
    companion object {
        const val BASE_URL = "https://www.breakingbadapi.com/api/"
        const val CHARACTERS_ENDPOINT = "characters"
    }

    @GET(CHARACTERS_ENDPOINT)
    suspend fun getAllCharacters(): Response<List<Character>>
}