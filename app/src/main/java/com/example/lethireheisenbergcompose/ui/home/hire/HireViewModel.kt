package com.example.lethireheisenbergcompose.ui.home.hire

import android.content.Context
import androidx.lifecycle.viewModelScope
import androidx.work.WorkManager
import com.example.lethireheisenbergcompose.MainViewModel
import com.example.lethireheisenbergcompose.data.AuthRepository
import com.example.lethireheisenbergcompose.data.HireRepository
import com.example.lethireheisenbergcompose.data.UserRepository
import com.example.lethireheisenbergcompose.model.Payment
import com.example.lethireheisenbergcompose.model.Hire
import com.example.lethireheisenbergcompose.model.HireStatus
import com.example.lethireheisenbergcompose.model.Pay
import com.example.lethireheisenbergcompose.model.Service
import com.example.lethireheisenbergcompose.model.ServiceProvider
import com.example.lethireheisenbergcompose.ui.profile.NotificationResolver
import com.example.lethireheisenbergcompose.utils.generateId
import com.example.lethireheisenbergcompose.workers.scheduleTimerWork
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class HireViewModel @Inject constructor(
    private val hireRepository: HireRepository,
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository
) : MainViewModel(userRepository, authRepository) {

    private val _serviceProvider: MutableStateFlow<ServiceProvider?> = MutableStateFlow(null)
    val serviceProvider: StateFlow<ServiceProvider?> get() = _serviceProvider

    private var _service = MutableStateFlow(Service.COOK)
    val service: StateFlow<Service> get() = _service

    private val _cost = MutableStateFlow(0.0)
    val cost: StateFlow<Double> get() = _cost

    private var _hourCounter = MutableStateFlow(1)
    val hourCounter: StateFlow<Int> get() = _hourCounter

    private var _selectedOption = MutableStateFlow(Pay.ALL_DOWN)
    private val selectedOption: StateFlow<Pay> get() = _selectedOption

    fun hireServiceProvider(context: Context, onComplete: () -> Unit) {
        viewModelScope.launch {
            serviceProvider.value?.let { sp ->
                val payment = Payment(hourCounter.value, cost.value, selectedOption.value)
                val hire = Hire(
                    generateId(),
                    sp.name,
                    service.value,
                    sp,
                    authRepository.currentUserUid,
                    payment,
                    HireStatus.PENDING
                )
                hireRepository.saveHireData(hire)

                // run worker manager
                val hours = payment.hours.toLong()
                val amount = payment.amount
                val rate = sp.rate
                user.value?.userId?.let {
                    val uuid = scheduleTimerWork(context, it, hours, amount, rate, payment.type.ordinal)
                    observeNewWork(uuid, hire, context) { onComplete() }
                }
            }
        }
    }

    private fun observeNewWork(uuid: UUID, hire: Hire, context: Context, onComplete: () -> Unit) {
        val workManager = WorkManager.getInstance()
        val workInfoLiveData = workManager.getWorkInfoByIdLiveData(uuid)
        workInfoLiveData.observeForever {
            if(it.state.isFinished) {
                endJob(context, hire)
                onComplete.invoke()
            }
        }
    }

    private fun endJob(context: Context, hire: Hire) {
        viewModelScope.launch {
            hire.endJob()
            hireRepository.updateHireData(hire.id, "status", hire.status.name)
            sendNotification(context, hire.serviceProvider.name)
        }
    }

    private fun sendNotification(context: Context, name: String) {
        // TODO
    }

    fun rehire(hire: Hire) {
       // TODO
    }

    private fun payForService() {
        // TODO
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

    fun onOptionSelected(option: Pay) {
        _selectedOption.value = option
    }
}