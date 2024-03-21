package com.example.lethireheisenbergcompose.network.breaking_bad_api


import com.example.lethireheisenbergcompose.model.Character
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class BreakingBadService @Inject constructor(private val breakingBadApi: BreakingBadApi){
    suspend fun getAllCharacters(): List<Character> {
        return withContext(Dispatchers.IO) {
            val response = breakingBadApi.getAllCharacters()
            val characters: List<Character> = response.body() ?: emptyList()
            characters
        }
    }
}