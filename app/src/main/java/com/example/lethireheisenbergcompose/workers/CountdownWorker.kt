package com.example.lethireheisenbergcompose.workers

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.lifecycle.LiveData
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.ListenableWorker
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.example.lethireheisenbergcompose.data.UserRepository
import com.example.lethireheisenbergcompose.model.Wallet
import com.example.lethireheisenbergcompose.ui.profile.NotificationResolver
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.delay
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton


@HiltWorker
class CountdownWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val userRepository: UserRepository) :
    CoroutineWorker(context, params) {

        val context = context

    override suspend fun doWork(): Result {
        val userId = inputData.getString("userId")
        val hours = inputData.getLong("hours", 0L)
        val amount = inputData.getDouble("amount", 0.0)


        println(this.id)
        delay(hours)
        if (userId != null) {
            pay(amount, userId)
            sendNotification("Kwota za zlecenie ${hours}h została pobrana: ${amount}$")
        }
        return Result.success()
    }

    private fun sendNotification(name: String) {
        NotificationResolver(context).showNotification("Zlecenie wykonane!", "Postać $name właśnie wykonała zadanie!")
    }
    private suspend fun pay(amount: Double, userId: String) {
        val wallet = getWalletFromDatabase(userId)
        wallet?.let {
            it.draw(amount)
            updateWalletInDatabase(userId, it)
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
}

fun scheduleTimerWork(
    context: Context,
    userId: String,
    hours: Long,
    amount: Double
): LiveData<WorkInfo> {

    val inputData = workDataOf(
        "userId" to userId,
        "hours" to hours,
        "amount" to amount
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
    return workManager.getWorkInfoByIdLiveData(workRequest.id)
}

@Singleton
class CountdownWorkerFactory @Inject constructor(
    private val userRepository: UserRepository
) : WorkerFactory() {

    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters
    ): ListenableWorker? {
        return CountdownWorker(appContext, workerParameters, userRepository)
    }
}

