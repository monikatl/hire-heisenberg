package com.example.lethireheisenbergcompose.di

import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class TestAppModule {
    // Możesz dostarczyć testowe obiekty, np. testowy ViewModel
}