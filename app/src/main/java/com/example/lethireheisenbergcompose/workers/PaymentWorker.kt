package com.example.lethireheisenbergcompose.workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters

private const val TAG = "PaymentWorker"

class PaymentWorker (ctx: Context, params: WorkerParameters) : CoroutineWorker(ctx, params) {
    override suspend fun doWork(): Result {
//        makeStatusNotification(
//            applicationContext.resources.getString(R.string.blurring_image),
//            applicationContext
//        )
        return Result.failure()
    }
}