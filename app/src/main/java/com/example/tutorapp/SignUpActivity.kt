package com.example.tutorapp

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.tutorapp.ui.login.LoginActivity
import java.util.*

class SignUpActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        Locale.setDefault(Locale.FRANCE)
        // Hide the action bar on this activity
        supportActionBar?.hide()
    }

    /**
     * onClick method of the datePickerButton
     * Method to select the date of birth
     */
    fun editDateOfBirth(view: View) {
        val datePickerButton = findViewById<Button>(R.id.signUp_datePickerButton)
        val c = Calendar.getInstance()
        val cYear = c.get(Calendar.YEAR)
        val cMonth = c.get(Calendar.MONTH)
        val cDay = c.get(Calendar.DAY_OF_MONTH)
        // Date Picker Dialog
        val dpd = DatePickerDialog(this, android.R.style.Theme_Material_Dialog_Alert, DatePickerDialog.OnDateSetListener { _, year, month, day ->
            val dayString = if (day<10) "0$day" else "$day"
            val monthNumber = month + 1
            val monthString = if (monthNumber<10) "0$monthNumber" else "$monthNumber"
            // Date to String
            val dateString = "${getString(R.string.birthday)} : $dayString/$monthString/$year"
            // Set the text value with the selected date
            datePickerButton.text = dateString
        }, cYear, cMonth, cDay)
        dpd.show()
    }

    /**
     * onClick method of the registerButton
     * Method to create a new user account and then go to the MainActivity
     */
    fun createAccount(view: View) {
        // TODO : handle the registration
        // Go to the main activity
        val mainActivityIntent = Intent(this, MainActivity::class.java)
        mainActivityIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(mainActivityIntent)
    }

    /**
     * onClick method of the signInButton
     * Method to go to the LoginActivity
     */
    fun goToLoginActivity(view: View) {
        val loginIntent = Intent(this, LoginActivity::class.java)
        startActivity(loginIntent)
    }
}