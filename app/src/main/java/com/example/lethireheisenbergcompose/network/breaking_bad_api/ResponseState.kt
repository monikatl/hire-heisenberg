package com.example.lethireheisenbergcompose.network.breaking_bad_api

import com.example.lethireheisenbergcompose.model.Character

sealed class ResponseState {
    class Success(val data: List<Character>) : ResponseState()
    class Failure(val msg: Throwable) : ResponseState()
    object Loading: ResponseState()
    object Empty: ResponseState()
}