package com.example.lethireheisenbergcompose.ui.profile

import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.lifecycle.viewModelScope
import com.example.lethireheisenbergcompose.MainViewModel
import com.example.lethireheisenbergcompose.R
import com.example.lethireheisenbergcompose.data.AuthRepository
import com.example.lethireheisenbergcompose.data.HireRepository
import com.example.lethireheisenbergcompose.data.OperationRepository
import com.example.lethireheisenbergcompose.data.UserRepository
import com.example.lethireheisenbergcompose.model.Hire
import com.example.lethireheisenbergcompose.model.HireStatus
import com.example.lethireheisenbergcompose.model.Operation
import com.example.lethireheisenbergcompose.model.ServiceProvider
import com.example.lethireheisenbergcompose.model.User
import com.example.lethireheisenbergcompose.model.Wallet
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val hireRepository: HireRepository,
    private val operationRepository: OperationRepository,
    private val authRepository: AuthRepository
) : MainViewModel(userRepository, authRepository) {



    private var _hires = MutableStateFlow(emptyList<Hire>())
    val hires: StateFlow<List<Hire>> get() = _hires

    private var _pendingHires = MutableStateFlow(emptyList<Hire>())
    val pendingHires: StateFlow<List<Hire>> get() = _pendingHires

    private var _historyHires = MutableStateFlow(emptyList<Hire>())
    val historyHires: StateFlow<List<Hire>> get() = _historyHires

    var _operationsHistory = MutableStateFlow(emptyList<Operation>())
    val operationHistory: StateFlow<List<Operation>> get() = _operationsHistory

    init {
        getHires()
    }

    fun changeCurrency() {
        TODO("Not yet implemented")
    }

    fun inputMoney(amount: Double) {
        _wallet.value?.let { wallet ->
            viewModelScope.launch {
                val newWallet = wallet.deposit(amount)
                userRepository.updateUserWallet(authRepository.currentUserUid, newWallet)
                wallet.lastOperation?.let {
                    operationRepository.saveOperationData(it)
                }
            }
        }
    }

    fun getHires() {
        viewModelScope.launch {
            hireRepository.getUserHires().collect {
                it?.let { hire ->
                    _hires.value = hire
                        .map { item -> resolveServiceProvidersForHires(item) }
                }
            }
            getAllPendingHires()
            getAllHistoryHires()
        }
    }

    fun getOperations() {
        viewModelScope.launch {
            user.value?.wallet?.id?.let {
                operationRepository.getWalletOperations(it).collect { list ->
                    _operationsHistory.value = list
                }
            }
        }
    }

    fun endJob(context: Context, hire: Hire) {
        viewModelScope.launch {
            hire.endJob()
            hireRepository.updateHireData(hire.id, "status", hire.status.name)
            sendNotification(context, hire.serviceProvider.name)
        }
    }

    private fun sendNotification(context: Context, name: String) {
        NotificationResolver(context).showNotification("Zlecenie wykonane!", "Postać $name właśnie wykonała zadanie!")
    }

    private fun resolveServiceProvidersForHires(hire: Hire): Hire {
        val serviceProvider = serviceProviders.value.firstOrNull() { it.name == hire.serviceProvider.name}
        hire.serviceProvider = serviceProvider ?: ServiceProvider("nikt")
        return hire
    }

    private fun filterHires(status: HireStatus): List<Hire> {
        return hires.value.filter { it.status == status }
    }

    private fun getAllHistoryHires(): StateFlow<List<Hire>> {
        _historyHires.value = filterHires(HireStatus.END)
        return historyHires
    }

    private fun getAllPendingHires() {
        _pendingHires.value =  filterHires(HireStatus.PENDING)
    }

}