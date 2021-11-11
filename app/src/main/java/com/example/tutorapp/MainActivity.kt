package com.example.tutorapp

import android.R.attr
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.tutorapp.ui.login.LoginActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import android.R.attr.bitmap

import android.graphics.drawable.BitmapDrawable

import android.graphics.drawable.Drawable




class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation_view)
        val navController = findNavController(R.id.nav_fragment)
        bottomNavigationView.setupWithNavController(navController)
        val tv = TypedValue()
        if (theme.resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            val actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, resources.displayMetrics)
            Log.d("Height of Action Bar 2", actionBarHeight.toString())
            val mIcon = BitmapFactory.decodeResource(getResources(),R.drawable.icon_round)
            val resizedBitmap = Bitmap.createScaledBitmap(mIcon, actionBarHeight, actionBarHeight, false)
            val mDrawable: Drawable = BitmapDrawable(resources, resizedBitmap)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.setHomeAsUpIndicator(mDrawable)
        }

        //val intent = Intent(this, LoginActivity::class.java)
        //startActivity(intent)
    }

}