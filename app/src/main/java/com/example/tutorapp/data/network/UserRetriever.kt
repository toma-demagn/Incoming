package com.example.tutorapp.data.network

import com.example.tutorapp.data.model.User
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * User retriever
 */
class UserRetriever {

    private val networkInterface: NetworkInterface

    init {
        val retrofit = Retrofit
            .Builder()
            .baseUrl(NetworkInterface.BaseURL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        networkInterface = retrofit.create(NetworkInterface::class.java)
    }

    suspend fun getUserById(id: Int): User {
        return networkInterface.getUserById(id)
    }

    suspend fun getUserByEmail(email: String): User {
        return networkInterface.getUserByEmail(email)
    }

    suspend fun createUser(user: User): User {
        return  networkInterface.createUser(user)
    }

    suspend fun updateUser(id: Int, user: User): User {
        return networkInterface.updateUser(id, user)
    }
}