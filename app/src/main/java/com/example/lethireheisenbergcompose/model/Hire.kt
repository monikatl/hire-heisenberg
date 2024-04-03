package com.example.lethireheisenbergcompose.model

class Hire (
    val id: String,
    val name: String,
    val service: Service,
    var serviceProvider: ServiceProvider,
    val userId: String,
    val duration: Payment,
    var status: HireStatus,
) {
    fun convertToDbHire(): DbHire {
        return DbHire(id, name, service.serviceName, serviceProvider.name, userId, duration.hours, duration.amount, status.name)
    }

    fun endJob() {
        serviceProvider.fire()
        status = HireStatus.END
    }

    companion object {
        fun convertToHire(dbHire: DbHire, serviceProvider: ServiceProvider): Hire {
            return with(dbHire) {
                Hire(
                    id,
                    name,
                    Service.COOK,
                    serviceProvider,
                    user,
                    Payment(counter, amount),
                    HireStatus.valueOf(dbHire.status))
            }
        }
    }
}