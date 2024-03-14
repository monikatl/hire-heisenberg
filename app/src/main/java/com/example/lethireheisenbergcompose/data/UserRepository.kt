package com.example.lethireheisenbergcompose.data

import com.example.lethireheisenbergcompose.model.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun getUserDetails(): Flow<User?>
    fun saveUserData(userId: String, username: String, email: String)
    fun updateUser(username: String)
    fun deleteUser()
}
