package com.example.lethireheisenbergcompose.ui.home.hire

import com.example.lethireheisenbergcompose.MainViewModel
import com.example.lethireheisenbergcompose.data.AuthRepository
import com.example.lethireheisenbergcompose.data.HireRepository
import com.example.lethireheisenbergcompose.data.UserRepository
import com.example.lethireheisenbergcompose.model.Payment
import com.example.lethireheisenbergcompose.model.Hire
import com.example.lethireheisenbergcompose.model.HireStatus
import com.example.lethireheisenbergcompose.model.Service
import com.example.lethireheisenbergcompose.model.ServiceProvider
import com.example.lethireheisenbergcompose.utils.generateId
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class HireViewModel @Inject constructor(
    private val hireRepository: HireRepository,
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository
) : MainViewModel(userRepository) {

    private val _serviceProvider: MutableStateFlow<ServiceProvider?> = MutableStateFlow(null)
    val serviceProvider: StateFlow<ServiceProvider?> get() = _serviceProvider

    private var _service = MutableStateFlow(Service.COOK)
    val service: StateFlow<Service> get() = _service

    private val _cost = MutableStateFlow(0.0)
    val cost: StateFlow<Double> get() = _cost

    private var _hourCounter = MutableStateFlow(1)
    val hourCounter: StateFlow<Int> get() = _hourCounter


    fun hireServiceProvider() {
        serviceProvider.value?.let { sp ->
            val duration = Payment(hourCounter.value, cost.value)
            val hire = Hire(
                generateId(),
                "blablabla",
                service.value,
                sp,
                authRepository.currentUserUid,
                duration,
                HireStatus.PENDING
            )
            hireRepository.saveHireData(hire)
            payForService()
        }
    }

    private fun payForService() {
        val wallet = user.value?.wallet
        wallet?.let {
            // pay all at start
            it.draw(cost.value)
            userRepository.updateUserWallet(authRepository.currentUserUid, it)
        }
    }

    fun updateCost() {
        _cost.value = serviceProvider.value?.rate?.times(hourCounter.value) ?: 0.0
    }

    fun addHour() {
        _hourCounter.value++
    }

    fun subHour() {
        _hourCounter.value--
    }

    fun setServiceProvider(serviceProvider: ServiceProvider) {
        _serviceProvider.value = serviceProvider
    }
}