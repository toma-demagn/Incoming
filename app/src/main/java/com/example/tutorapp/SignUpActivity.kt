package com.example.tutorapp

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.tutorapp.ui.login.LoginActivity
import java.util.*
import kotlin.collections.ArrayList

class SignUpActivity : AppCompatActivity() {

    private lateinit var editTextList: ArrayList<EditText>
    private var dateOfBirthValue = ""
    private var iCount = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        Locale.setDefault(Locale.FRANCE)
        // Hide the action bar on this activity
        supportActionBar?.hide()

        // Init the edit text inputs
        val firstNameEditText = findViewById<EditText>(R.id.signUp_firstNameEditText)
        val lastNameEditText = findViewById<EditText>(R.id.signUp_lastNameEditText)
        val emailEditText = findViewById<EditText>(R.id.signUp_emailEditText)
        val usernameEditText = findViewById<EditText>(R.id.signUp_usernameEditText)
        val passwordEditText = findViewById<EditText>(R.id.signUp_passwordEditText)
        // Create a list of all edit text
        editTextList = arrayListOf(firstNameEditText, lastNameEditText, emailEditText, usernameEditText, passwordEditText)

        // Add a listener on each editText
        for (e in editTextList) {
            e.addTextChangedListener(object: TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun afterTextChanged(p0: Editable?) {
                    checkInputsValues()
                }
            })
        }
    }

    /**
     * Method to check all the inputs values in order to enable or disable the register button
     */
    fun checkInputsValues() {
        var isDataValid = true
        // Check the editText inputs
        for (e in editTextList) {
            if (e.text.isNullOrEmpty()) {
                isDataValid = false
            }
        }
        // Check the date of birth value (only if the isDataValid value is still true)
        if (isDataValid && dateOfBirthValue.isEmpty()) {
            isDataValid = false
        }
        // Enable or disable the register button
        val registerButton = findViewById<Button>(R.id.signUp_registerButton)
        registerButton.isEnabled = isDataValid
        iCount++
        Log.d("CHECK_INPUT", iCount.toString())
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
            dateOfBirthValue = "$dayString/$monthString/$year"
            val dateString = "${getString(R.string.dateOfBirth)} : $dateOfBirthValue"
            // Set the text value with the selected date
            datePickerButton.text = dateString
            // After a date has been selected, check the inputs to enable or not the register button
            checkInputsValues()
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