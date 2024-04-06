package com.example.lethireheisenbergcompose.data

import com.example.lethireheisenbergcompose.model.DbUser
import com.example.lethireheisenbergcompose.model.User
import com.example.lethireheisenbergcompose.model.Wallet
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun getUserDetails(): Flow<User?>

    fun getWalletDetails(userId: String): Flow<Wallet?>
    fun saveUserData(userId: String, username: String, email: String)
    fun updateUser(updatedData: Map<String, String>)
    fun updateUserWallet(userId: String, updatedWallet: Wallet)
    fun deleteUser()
}
