package com.example.lethireheisenbergcompose.di

import com.example.lethireheisenbergcompose.network.breaking_bad_api.BreakingBadApi
import com.example.lethireheisenbergcompose.network.breaking_bad_api.BreakingBadRepository
import com.example.lethireheisenbergcompose.network.breaking_bad_api.BreakingBadService
import com.example.lethireheisenbergcompose.network.breaking_bad_api.GetCharactersUseCase
import com.example.lethireheisenbergcompose.network.quotes_api.QuotesApi
import com.example.lethireheisenbergcompose.network.quotes_api.QuotesRepository
import com.example.lethireheisenbergcompose.network.quotes_api.QuotesService
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofitBreakingBad(okHttpClient: OkHttpClient): BreakingBadApi{
        val gson = GsonBuilder().setLenient().create()
        return Retrofit.Builder()
            .baseUrl(BreakingBadApi.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(BreakingBadApi::class.java)
    }

    @Provides
    @Singleton
    fun provideBreakingBadService(breakingBadApi: BreakingBadApi): BreakingBadService {
        return BreakingBadService(breakingBadApi)
    }


    @Provides
    @Singleton
    fun provideBreakingBadRepository(breakingBadService: BreakingBadService): BreakingBadRepository {
        return BreakingBadRepository(breakingBadService)
    }

    @Provides
    @Singleton
    fun provideCharactersUseCase(breakingBadRepository: BreakingBadRepository): GetCharactersUseCase {
        return GetCharactersUseCase(breakingBadRepository)
    }

    @Provides
    @Singleton
    fun provideRetrofitQuotes(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(QuotesApi.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideQuotesApi(retrofit: Retrofit): QuotesApi {
        return retrofit.create(QuotesApi::class.java)
    }

    @Provides
    @Singleton
    fun provideQuotesService(quotesApi: QuotesApi): QuotesService {
        return QuotesService(quotesApi)
    }


    @Provides
    @Singleton
    fun provideQuotesRepository(quotesService: QuotesService): QuotesRepository {
        return QuotesRepository(quotesService)
    }
}