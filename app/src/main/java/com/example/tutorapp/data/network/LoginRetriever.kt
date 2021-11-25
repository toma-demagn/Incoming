package com.example.tutorapp.data.network

import com.example.tutorapp.data.model.Login
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class LoginRetriever {

    private val networkInterface: NetworkInterface

    init {
        val retrofit = Retrofit
            .Builder()
            .baseUrl(NetworkInterface.BaseURl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        networkInterface = retrofit.create(NetworkInterface::class.java)
    }

    suspend fun createLogin(login: Login): Login {
        return networkInterface.createLogin(login)
    }

    suspend fun deleteLogin(id: String) {
        return networkInterface.deleteLogin(id)
    }
}