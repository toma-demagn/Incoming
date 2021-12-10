package com.example.tutorapp.data.network

import com.example.tutorapp.data.model.Ad
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Ad retriever
 */
class AdRetriever {

    private val networkInterface: NetworkInterface

    init {
        val retrofit = Retrofit
            .Builder()
            .baseUrl(NetworkInterface.BaseURL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        networkInterface = retrofit.create(NetworkInterface::class.java)
    }

    suspend fun getAds(): List<Ad> {
        return networkInterface.getAds()
    }

    suspend fun getAdById(id: Int): Ad {
        return networkInterface.getAdById(id)
    }

    suspend fun createAd(ad: Ad): Ad {
        return networkInterface.createAd(ad)
    }
}