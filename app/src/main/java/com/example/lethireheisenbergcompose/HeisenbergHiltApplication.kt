package com.example.lethireheisenbergcompose

import android.app.Application
import androidx.work.Configuration
import com.example.lethireheisenbergcompose.workers.CountdownWorkerFactory
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class HeisenbergHiltApplication : Application(), Configuration.Provider {
    @Inject
    lateinit var countdownWorkerFactory: CountdownWorkerFactory

    override fun getWorkManagerConfiguration(): Configuration {
        return Configuration.Builder()
            .setWorkerFactory(countdownWorkerFactory)
            .build()
    }
}