package com.example.lethireheisenbergcompose.di

import com.example.lethireheisenbergcompose.data.AuthRepository
import com.example.lethireheisenbergcompose.data.AuthRepositoryImpl
import com.example.lethireheisenbergcompose.data.HireRepository
import com.example.lethireheisenbergcompose.data.HireRepositoryImpl
import com.example.lethireheisenbergcompose.data.UserRepository
import com.example.lethireheisenbergcompose.data.UserRepositoryImpl
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
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
    fun provideFirestore() = FirebaseFirestore.getInstance()


    @Provides
    @Singleton
    fun providesAuthRepositoryImpl(firebaseAuth: FirebaseAuth): AuthRepository {
        return AuthRepositoryImpl(firebaseAuth = firebaseAuth)
    }

    @Provides
    @Singleton
    fun providesUserRepositoryImpl(firebaseFirestore: FirebaseFirestore, firebaseAuth: FirebaseAuth, authRepository: AuthRepository): UserRepository {
        return UserRepositoryImpl(firebaseFirestore, firebaseAuth, authRepository)
    }

    @Provides
    @Singleton
    fun providesHireRepositoryImpl(firebaseFirestore: FirebaseFirestore, authRepository: AuthRepository): HireRepository {
        return HireRepositoryImpl(firebaseFirestore, authRepository)
    }

}