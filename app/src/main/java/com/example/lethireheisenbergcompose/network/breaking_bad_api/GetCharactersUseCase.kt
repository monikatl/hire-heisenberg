package com.example.lethireheisenbergcompose.network.breaking_bad_api

import com.example.lethireheisenbergcompose.model.ServiceProvider
import com.example.lethireheisenbergcompose.network.breaking_bad_api.BreakingBadRepository
import javax.inject.Inject

class GetCharactersUseCase @Inject constructor(
    private val breakingBadRepository: BreakingBadRepository
){
    suspend operator fun invoke(): List<ServiceProvider> {
        return breakingBadRepository.getAllServiceProviders()
    }
}
