package com.example.lethireheisenbergcompose.workers

import android.os.CountDownTimer
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.lethireheisenbergcompose.model.Payment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class CountdownWorker(context: android.content.Context, params: WorkerParameters) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        val hours = inputData.getInt("hours", 0)
        val newHours = hours - 1
        if (newHours >= 0) {
            // Aktualizuj pozostałą liczbę godzin w interfejsie użytkownika (np. przez LiveData)
            GlobalScope.launch(Dispatchers.Main) {
                // Aktualizacja interfejsu użytkownika
                // W rzeczywistej aplikacji należy używać mechanizmów zarządzania stanem, takich jak ViewModel
                // Tutaj użyto tylko w celach demonstracyjnych
                // updateRemainingHours(newHours)
            }

            // Jeśli pozostały czas wynosi 0, zakończ odliczanie
            if (newHours == 0) {
                // Wyślij powiadomienie o zakończeniu odliczania (np. przez NotificationManager)
            }
        }

        return Result.success()
    }

    fun countDownTimer(payment: Payment) {
        var remainingTime = payment.hourCounter
        val coroutineScope = CoroutineScope(Dispatchers.Default)

        fun startTimer(seconds: Int) {
            coroutineScope.launch {
                val timer = object : CountDownTimer(seconds * 1000L, 1000) {
                    override fun onTick(millisUntilFinished: Long) {
                        remainingTime = (millisUntilFinished / 1000).toInt()
                    }

                    override fun onFinish() {
                        remainingTime = 0
                    }
                }
                timer.start()
            }
        }
    }
}