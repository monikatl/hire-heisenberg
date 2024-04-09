package com.example.lethireheisenbergcompose.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.lethireheisenbergcompose.MainViewModel
import com.example.lethireheisenbergcompose.data.AuthRepository
import com.example.lethireheisenbergcompose.data.UserRepository
import com.example.lethireheisenbergcompose.model.Character
import com.example.lethireheisenbergcompose.model.Quote
import com.example.lethireheisenbergcompose.model.Service
import com.example.lethireheisenbergcompose.model.ServiceProvider
import com.example.lethireheisenbergcompose.model.User
import com.example.lethireheisenbergcompose.model.Wallet
import com.example.lethireheisenbergcompose.network.breaking_bad_api.BreakingBadApi
import com.example.lethireheisenbergcompose.network.breaking_bad_api.GetCharactersUseCase
import com.example.lethireheisenbergcompose.network.quotes_api.QuotesApi
import com.example.lethireheisenbergcompose.network.quotes_api.QuotesRepository
import com.example.lethireheisenbergcompose.utils.generateId
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel
@Inject constructor(
    private val quotesRepository: QuotesRepository,
    private val userRepository: UserRepository,
    val authRepository: AuthRepository
) : MainViewModel(userRepository, authRepository) {

    private val _isSearching = MutableStateFlow(false)
    val isSearching = _isSearching.asStateFlow()

    private val _searchText = MutableStateFlow("")
    val searchText = _searchText.asStateFlow()


    private val _quote = MutableStateFlow(Quote("", ""))
    val quote: StateFlow<Quote> get() = _quote

    init {
        getQuote()
        getUser()
    }

    fun getQuote() {
        viewModelScope.launch {
            try {
                val quote = quotesRepository.getRandomQuote()
                _quote.value = quote
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private val _categories = MutableStateFlow(Service.entries.map { it.serviceName })
    val categories = searchText
        .combine(_categories) { text, c ->
            c.filter { category ->
                category.uppercase().contains(text.trim().uppercase())
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = _categories.value
        )

    fun onSearchTextChange(text: String) {
        _searchText.value = text
    }

    fun onToogleSearch() {
        _isSearching.value = !_isSearching.value
        if (!_isSearching.value) {
            onSearchTextChange("")
        }
    }
}