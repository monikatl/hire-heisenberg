package com.example.lethireheisenbergcompose.network.breaking_bad_api

import com.example.lethireheisenbergcompose.model.ServiceProvider
import javax.inject.Inject

class BreakingBadRepository@Inject constructor(private val productService: BreakingBadService) {
    suspend fun getAllServiceProviders(): List<ServiceProvider> {
        return productService.getAllCharacters().map {
            ServiceProvider.createServiceProvider(it)
        }
    }
}