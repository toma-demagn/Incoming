package com.example.tutorapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_show_ad.*

class ShowAd : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_ad)
        val bundle = intent.extras
        val name = bundle?.getString("name")
        val desc = bundle?.getString("desc")
        val pic = bundle?.getInt("pic")
        textView9.setText(name)
        descriptionText.setText(desc)
        if (pic != null) {
            imageView.setImageResource(pic)
        }
        contactButton.setOnClickListener {
            Toast.makeText(this, "Sandra a été contactée", Toast.LENGTH_SHORT).show()
        }
    }
}