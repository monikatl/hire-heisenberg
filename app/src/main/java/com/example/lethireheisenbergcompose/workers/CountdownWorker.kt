package com.example.lethireheisenbergcompose.workers

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.ListenableWorker
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.example.lethireheisenbergcompose.data.OperationRepository
import com.example.lethireheisenbergcompose.data.UserRepository
import com.example.lethireheisenbergcompose.model.Operation
import com.example.lethireheisenbergcompose.model.Wallet
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.UUID
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton


@HiltWorker
class CountdownWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val userRepository: UserRepository,
    private val operationRepository: OperationRepository) :
    CoroutineWorker(context, params) {

        val context = context

    private val lock = Any()
    override suspend fun doWork(): Result {
        val userId = inputData.getString("userId")
        val hours = inputData.getLong("hours", 0L)
        val amount = inputData.getDouble("amount", 0.0)
        val rate = inputData.getDouble("rate", 0.0)
        val paymentStrategy = inputData.getInt("paymentStrategy", 0)

        resolvePayStrategy(hours, userId, amount, rate, paymentStrategy)
        return Result.success()
    }

    private suspend fun resolvePayStrategy(hours: Long, userId: String?, amount: Double, rate: Double, paymentStrategy: Int) {
        when(paymentStrategy) {
            0 -> hourlyPayUp(hours, userId, rate)
            1 -> hourlyPayDown(hours, userId, rate)
            2 -> wholePayUp(hours, userId, amount)
            3 -> wholePayDown(hours, userId, amount)
        }
    }

    private suspend fun hourlyPayUp(hours: Long, userId: String?, amount: Double) {
        for (hour in 0..<hours) {
            pay(amount, userId)
            delay(1000)
        }
    }

    private suspend fun hourlyPayDown(hours: Long, userId: String?, amount: Double) {
        for (hour in 0..<hours) {
            delay(1000)
            pay(amount, userId)
        }
    }

    private suspend fun wholePayDown(hours: Long, userId: String?, amount: Double) {
        delay(hours*1000)
        pay(amount, userId)
    }

    private suspend fun wholePayUp(hours: Long, userId: String?, amount: Double) {
        pay(amount, userId)
        delay(hours*1000)
    }

    private fun pay(amount: Double, userId: String?) {
        synchronized(lock) {
            if (userId != null) {
                GlobalScope.launch {
                    val wall = getWalletFromDatabase(userId)
                 wall?.let { wallet ->
                    wallet.draw(amount)
                    updateWalletInDatabase(userId, wallet)
                        wallet.lastOperation?.let { operation ->
                            addDrawOperationToRepository(operation)
                        }
                    }
                }
            }
        }
    }

    private suspend fun getWalletFromDatabase(userId: String): Wallet? {
        var wallet: Wallet? = null
        userRepository.getWalletDetails(userId).collect {
            wallet = it
        }
        return wallet
    }

    private fun updateWalletInDatabase(userId: String, updatedWallet: Wallet) {
        userRepository.updateUserWallet(userId, updatedWallet)
    }

    private fun addDrawOperationToRepository(operation: Operation) {
        operationRepository.saveOperationData(operation)
    }
}

fun scheduleTimerWork(
    context: Context,
    userId: String,
    hours: Long,
    amount: Double,
    rate: Double,
    paymentStrategy: Int
) : UUID {

    val inputData = workDataOf(
        "userId" to userId,
        "hours" to hours,
        "amount" to amount,
        "rate" to rate,
        "paymentStrategy" to paymentStrategy
    )

    val constraints = Constraints.Builder()
        .setRequiredNetworkType(NetworkType.NOT_REQUIRED)
        .setRequiresBatteryNotLow(true)
        .build()

    val workRequest = OneTimeWorkRequestBuilder<CountdownWorker>()
        .setConstraints(constraints)
        .setInputData(inputData)
        .setInitialDelay(hours, TimeUnit.MILLISECONDS)
        .build()

    val workManager = WorkManager.getInstance(context)
    workManager.enqueue(workRequest)
    return workRequest.id
}

@Singleton
class CountdownWorkerFactory @Inject constructor(
    private val userRepository: UserRepository,
    private val operationRepository: OperationRepository
) : WorkerFactory() {

    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters
    ): ListenableWorker {
        return CountdownWorker(appContext, workerParameters, userRepository, operationRepository)
    }
}

