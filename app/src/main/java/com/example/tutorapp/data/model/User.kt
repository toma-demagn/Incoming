package com.example.tutorapp.data.model

/**
 * Data class for User object
 */
data class User (
    val firstName: String,
    val lastName: String,
    val email: String,
    val username: String,
    val birthDate: String,
    val profileImgPath: String = "https://upload.wikimedia.org/wikipedia/commons/9/99/Sample_User_Icon.png",
    val targetWantAd: Boolean = true,
    val targetTutoringAd: Boolean = true
)