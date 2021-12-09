package com.example.tutorapp.data.model

data class Ad(
    val id: Int?= null,
    val isTutoringAd: Boolean,
    val title: String,
    val description: String,
    val availabilities: String,
    val subject: String,
    val educationLevel: String,
    val location: String,
    val authorId: Int,
    val creationTime: String,
    val imgPath: String = "https://static.thenounproject.com/png/192605-200.png"
)