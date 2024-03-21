package com.example.lethireheisenbergcompose.model

class User {
    var userId: String = ""
    var name: String = ""
    var email: String = ""

    var wallet: Wallet = Wallet()

    fun convertToDbUser(): DbUser {
        return DbUser(userId, name, email, wallet.convertToDbWallet())
    }

    companion object {
        fun convertToUser(dbUser: DbUser): User {
            val user = User().apply {
                userId = dbUser.userId
                name = dbUser.name
                email = dbUser.email
                dbUser.wallet?.let {
                    wallet = Wallet.convertToWallet(it)
                }
            }
            return user
        }
    }
}
