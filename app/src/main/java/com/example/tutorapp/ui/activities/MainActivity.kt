package com.example.tutorapp.ui.activities

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.tutorapp.R
import com.example.tutorapp.ui.fragments.*
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
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_fragment_container) as NavHostFragment
        val navController = navHostFragment.navController
        bottomNavigationView.setupWithNavController(navController)
        bottomNavigationView.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.homeFragment -> switchToFragment(HomeFragment())
                R.id.searchFragment -> switchToFragment(SearchFragment())
                R.id.adCreationFragment -> switchToFragment(AdCreationFragment())
                R.id.messagingFragment -> switchToFragment(SocketsFragment())
                R.id.accountFragment -> switchToFragment(AccountFragment())
                else -> true
            }
        }
    }

    private fun switchToFragment(fragment: Fragment): Boolean {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.nav_fragment_container, fragment)
        transaction.commit()
        return true
    }

}