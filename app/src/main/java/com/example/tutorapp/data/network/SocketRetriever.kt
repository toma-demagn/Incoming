package com.example.tutorapp.data.network

import com.example.tutorapp.data.model.Socket
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SocketRetriever {

    private val networkInterface: NetworkInterface

    init {
        val retrofit = Retrofit
            .Builder()
            .baseUrl(NetworkInterface.BaseURL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        networkInterface = retrofit.create(NetworkInterface::class.java)
    }

    suspend fun getSocketById(id: Int): Socket {
        return networkInterface.getSocketById(id)
    }

    suspend fun getSocketsByUserId(userId: Int): List<Socket> {
        return networkInterface.getSocketsByUserId(userId)
    }

    suspend fun createSocket(socket: Socket): Socket {
        return networkInterface.createSocket(socket)
    }

}