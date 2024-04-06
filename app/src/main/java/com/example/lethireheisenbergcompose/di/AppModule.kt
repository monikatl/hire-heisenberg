package com.example.lethireheisenbergcompose.di

import androidx.work.WorkerFactory
import com.example.lethireheisenbergcompose.workers.CountdownWorkerFactory
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface AppModule {

    @Binds
    fun bindWorkerFactory(factory: CountdownWorkerFactory): WorkerFactory
}