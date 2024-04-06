package com.example.lethireheisenbergcompose.ui.home.hire

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.example.lethireheisenbergcompose.MainViewModel
import com.example.lethireheisenbergcompose.data.AuthRepository
import com.example.lethireheisenbergcompose.data.HireRepository
import com.example.lethireheisenbergcompose.data.UserRepository
import com.example.lethireheisenbergcompose.model.Payment
import com.example.lethireheisenbergcompose.model.Hire
import com.example.lethireheisenbergcompose.model.HireStatus
import com.example.lethireheisenbergcompose.model.Service
import com.example.lethireheisenbergcompose.model.ServiceProvider
import com.example.lethireheisenbergcompose.ui.profile.NotificationResolver
import com.example.lethireheisenbergcompose.utils.generateId
import com.example.lethireheisenbergcompose.workers.scheduleTimerWork
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
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

    private val _workInfo: MutableLiveData<WorkInfo> = MutableLiveData()

    val workInfo: LiveData<WorkInfo> = _workInfo




    fun hireServiceProvider(context: Context) {
        serviceProvider.value?.let { sp ->
            val payment= Payment(hourCounter.value, cost.value)
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
            val hours = (payment.hourCounter * 1000).toLong()
            val amount = payment.amount
            user.value?.userId?.let {
                scheduleTimerWork(context, it, hours, amount)
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

    fun rehire(hire: Hire) {
        val payment= Payment(hire.duration.hourCounter, hire.duration.amount)
        val newHire = with(hire) {
           Hire(
               generateId(),
               name,
               service,
               serviceProvider,
               userId,
               payment,
               HireStatus.PENDING
           )
        }
        hireRepository.saveHireData(newHire)
        payForService()
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

//    fun observeWorkInfo() {
//        viewModelScope.launch {
//            supervisorScope {
//                launch {
//                    Log.d("TAG", "WorkRequest - state observation - start")
//                    workInfo.asFlow().collect {
//                        Log.d("TAG", "WorkRequest - ${it?.state?.name!!}")
//                        if (it.state.isFinished) {
//                            Log.d("TAG", "WorkRequest - Work finished")
//                            cancel() // cancel the supervisorScope, it is now redundant
//                        }
//                    }
//                }
//            }.invokeOnCompletion {
//                Log.d("TAG", "WorkRequest - state observation - end")
//            }
//        }
//    }

}