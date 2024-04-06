package com.example.lethireheisenbergcompose

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lethireheisenbergcompose.data.UserRepository
import com.example.lethireheisenbergcompose.model.Character
import com.example.lethireheisenbergcompose.model.Hire
import com.example.lethireheisenbergcompose.model.ServiceProvider
import com.example.lethireheisenbergcompose.model.User
import com.example.lethireheisenbergcompose.network.breaking_bad_api.GetCharactersUseCase
import com.example.lethireheisenbergcompose.ui.profile.NotificationResolver
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import java.net.URL
import kotlinx.serialization.decodeFromString
import javax.inject.Inject

@HiltViewModel
open class MainViewModel @Inject constructor(private val userRepository: UserRepository, private val getCharactersUseCase: GetCharactersUseCase? = null) : ViewModel() {


    protected val _user: MutableStateFlow<User?> = MutableStateFlow(null)
    val user: StateFlow<User?> = _user

    private val _serviceProviders = MutableStateFlow(listOf<ServiceProvider>())
    val serviceProviders: StateFlow<List<ServiceProvider>> get() = _serviceProviders


    init {
        getUser()
        //getServiceProviders()
        fetchCharacters()
    }

    fun getUser() {
        viewModelScope.launch {
            userRepository.getUserDetails().collect { fetchedUser ->
                _user.value = fetchedUser
            }
        }
    }

    private fun fetchCharacters() {
        viewModelScope.launch(Dispatchers.IO) {
            val json = URL("https://gist.githubusercontent.com/glitchedmob/373e1ebf69d2c90cbf1a98322b0d77b7/raw/2ab6ab26f6dd9b763ff9fe725454ed7e4492f80d/breakingbadcharacters.json").readText()
            val characters = Json.decodeFromString<List<Character>>(json)
            _serviceProviders.value = characters.map { ServiceProvider.createServiceProvider(it) }
        }
    }


//    fun getServiceProviders() {
//        viewModelScope.launch {
//            try {
//                val serviceProviders = getCharactersUseCase?.invoke()
//                _serviceProviders.value = serviceProviders ?: emptyList()
//            } catch (e: Exception) {
//
//                e.printStackTrace()
//            }
//        }
//    }
}