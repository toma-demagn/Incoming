package com.example.tutorapp.data.network

import com.example.tutorapp.data.model.Tag
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class TagRetriever {
    private val networkInterface: NetworkInterface

    companion object {
        var BaseURl = "https://apitutorapp.herokuapp.com"
    }

    init {
        val retrofit = Retrofit
            .Builder()
            .baseUrl(BaseURl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        networkInterface = retrofit.create(NetworkInterface::class.java)
    }

    suspend fun getTags(): List<Tag> {
        return networkInterface.getTags()
    }
}