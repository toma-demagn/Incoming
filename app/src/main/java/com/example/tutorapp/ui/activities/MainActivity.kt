package com.example.tutorapp.ui.activities

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.tutorapp.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var sp: SharedPreferences
    private var userId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // We get the sp values
        sp = getSharedPreferences("login", MODE_PRIVATE)
        userId = sp.getInt("userId", -1)

        // If the userId is equal to -1, it means that the user is not logged in or there is an error
        // So we go back to the signUp activity and we reset the sp values
        if (userId == -1) {
            sp.edit().putBoolean("isLoggedIn", false).apply()
            sp.edit().putInt("userId", -1).apply()
            val signUpActivityIntent = Intent(this, SignUpActivity::class.java)
            signUpActivityIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(signUpActivityIntent)
        }

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation_view)
        val navController = findNavController(R.id.nav_fragment)
        bottomNavigationView.setupWithNavController(navController)
    }

}