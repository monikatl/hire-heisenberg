package com.example.lethireheisenbergcompose.model

import com.example.lethireheisenbergcompose.utils.generateId
import kotlin.random.Random


class ServiceProvider (
    val name: String,
    val figure: Character? = null,
    val rate: Double = 0.0,
    private var isFree: Boolean = true,
    val services: List<Service> = emptyList()
) {
    fun hire() {
        this.isFree = false
    }

    fun fire() {
        this.isFree = true
    }

    companion object {
        fun createServiceProvider(figure: Character): ServiceProvider {
            val rate = resolveSPRate()
            val services = resolveSPServices()
            return ServiceProvider(figure.name, figure, rate, true, services)
        }

        // TODO LHH-23
        private fun resolveSPServices(): List<Service> {
            val randomIndex = (0..3).random()
            return listOf(Service.entries[randomIndex])
        }

        // TODO LHH-22
        private fun resolveSPRate(): Double {
            return (15..200).random().toDouble()
        }
    }
}