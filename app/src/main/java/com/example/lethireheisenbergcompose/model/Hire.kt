package com.example.lethireheisenbergcompose.model

class Hire (
    val id: String,
    val name: String,
    val service: Service,
    val serviceProvider: ServiceProvider,
    val userId: String,
    val duration: Payment,
    val status: HireStatus,
) {
    fun convertToDbHire(): DbHire {
        return DbHire(id, name, service.serviceName, serviceProvider.id, userId, duration.hours, duration.amount, status.name)
    }
}