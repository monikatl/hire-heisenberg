package com.example.lethireheisenbergcompose.model

import com.example.lethireheisenbergcompose.utils.generateId


class ServiceProvider (
    val id: String,
    val figure: Character,
    val rate: Double,
    private var isFree: Boolean = true,
    val services: List<Service> = emptyList()) {



    fun hire() {
        this.isFree = false
    }

    companion object {
        fun createServiceProvider(figure: Character): ServiceProvider {
            val rate = resolveSPRate()
            val services = resolveSPServices()
            return ServiceProvider(generateId(), figure, rate, true, services)
        }

        // TODO LHH-23
        private fun resolveSPServices(): List<Service> {
            return emptyList()
        }

        // TODO LHH-22
        private fun resolveSPRate(): Double {
            return 0.0
        }
    }
}