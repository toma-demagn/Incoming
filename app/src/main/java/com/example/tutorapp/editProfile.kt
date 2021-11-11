package com.example.tutorapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_edit_profile.*

class editProfile : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)
        saveChangesProfileButton.setOnClickListener {
            Toast.makeText(this, "Changements Appliqués", Toast.LENGTH_LONG).show()
        }
    }
}