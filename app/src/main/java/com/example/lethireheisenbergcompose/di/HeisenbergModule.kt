package com.example.lethireheisenbergcompose.di

import com.example.lethireheisenbergcompose.data.AuthRepository
import com.example.lethireheisenbergcompose.data.AuthRepositoryImpl
import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object HeisenbergModule {
    @Provides
    @Singleton
    fun providesFirebaseAuth() = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun providesAuthRepositoryImpl(firebaseAuth: FirebaseAuth): AuthRepository {
        return AuthRepositoryImpl(firebaseAuth = firebaseAuth)
    }

//    @Provides
//    @Singleton
//    fun providesDatabaseRepositoryImpl(): DatabaseRepository {
//        return DatabaseRepositoryImpl()
//    }

}