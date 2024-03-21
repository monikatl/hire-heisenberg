package com.example.lethireheisenbergcompose.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.lethireheisenbergcompose.MainViewModel
import com.example.lethireheisenbergcompose.data.UserRepository
import com.example.lethireheisenbergcompose.model.DbUser
import com.example.lethireheisenbergcompose.model.User
import com.example.lethireheisenbergcompose.model.Wallet
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val repository: UserRepository
) : MainViewModel(repository) {

    fun changeCurrency() {
        TODO("Not yet implemented")
    }

    fun inputMoney(amount: Double) {
        _user.value?.let { user ->
            viewModelScope.launch {
                val wallet = user.wallet.deposit(amount)
                repository.updateUserWallet(user.userId, wallet)
            }
        }
    }
}