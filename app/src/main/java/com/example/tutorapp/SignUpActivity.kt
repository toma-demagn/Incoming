package com.example.tutorapp

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.tutorapp.ui.login.LoginActivity
import java.util.*

class SignUpActivity : AppCompatActivity() {

    private lateinit var editTextList: ArrayList<EditText>
    private var dateOfBirthValue = ""

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
        val confirmPasswordEditText = findViewById<EditText>(R.id.signUp_confirmPasswordEditText)
        // Create a list of all edit text
        editTextList = arrayListOf(
            firstNameEditText, lastNameEditText, emailEditText,
            usernameEditText, passwordEditText, confirmPasswordEditText
        )

        // Add a listener on each editText
        for (e in editTextList) {
            e.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun afterTextChanged(p0: Editable?) {
                    handleSignUpButtonState()
                }
            })
        }
    }

    /**
     * Check that all fields are completed
     * Returns true if it is the case, false otherwise
     */
    private fun areAllFieldsCompleted(): Boolean {
        var allFieldsCompleted = true
        // Check the editText inputs
        for (e in editTextList) {
            if (e.text.isNullOrEmpty()) {
                allFieldsCompleted = false
            }
        }
        // Check the date of birth value (only if the isDataValid value is still true)
        if (allFieldsCompleted && dateOfBirthValue.isEmpty()) {
            allFieldsCompleted = false
        }
        // Return the boolean
        return allFieldsCompleted
    }

    /**
     * Check that the values of the two password fields are identical
     * Returns true if it is the case, false otherwise
     */
    private fun areThePasswordFieldsIdentical(): Boolean {
        val passwordValue: String = editTextList[4].text.toString()
        val confirmPasswordEditText = editTextList[5]
        val confirmPasswordValue: String = confirmPasswordEditText.text.toString()
        val areIdentical = passwordValue == confirmPasswordValue
        // If the two values are not identical, we display an error message to inform the user
        if (!areIdentical) {
            confirmPasswordEditText.error = "Doit être identique au mot de passe renseigné."
        }
        return areIdentical
    }

    /**
     * Check that the value in the email field contains the right characters.
     * Returns true if it is the case, false otherwise
     */
    private fun isTheEmailWellFormatted(): Boolean {
        val emailEditText = editTextList[2]
        val emailValue = emailEditText.text.toString()
        val res = emailValue.contains('@') && emailValue.split('@')[1].contains('.')
        if (!res) {
            emailEditText.error = "L'email renseigné doit être au bon format."
        }
        return res
    }

    /**
     * Check that the value in the username and password fields have the required length.
     * Returns true if it is the case, false otherwise
     */
    private fun areTheFieldsLongEnough(): Boolean {
        // Username
        val usernameEditText = editTextList[3]
        val isUsernameLongEnough = usernameEditText.text.toString().length > 2
        if (!isUsernameLongEnough) {
            usernameEditText.error = "Le nom d'utilisateur doit au minimum contenir 3 caractères."
        }
        // Password
        val passwordEditText = editTextList[4]
        val isPasswordLongEnough = passwordEditText.text.toString().length > 7
        if (!isPasswordLongEnough) {
            passwordEditText.error = "Le mot de passe doit au minimum contenir 8 caractères."
        }
        // Return the result
        return isUsernameLongEnough && isPasswordLongEnough
    }

    /**
     * Enable or not the sign up button according to the fields values
     */
    fun handleSignUpButtonState() {
        val registerButton = findViewById<Button>(R.id.signUp_registerButton)
        registerButton.isEnabled = areAllFieldsCompleted() && areThePasswordFieldsIdentical()
                && isTheEmailWellFormatted() && areTheFieldsLongEnough()
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
        val dpd = DatePickerDialog(
            this,
            android.R.style.Theme_Material_Dialog_Alert,
            DatePickerDialog.OnDateSetListener { _, year, month, day ->
                val dayString = if (day < 10) "0$day" else "$day"
                val monthNumber = month + 1
                val monthString = if (monthNumber < 10) "0$monthNumber" else "$monthNumber"
                // Date to String
                dateOfBirthValue = "$dayString/$monthString/$year"
                val dateString = "${getString(R.string.dateOfBirth)} : $dateOfBirthValue"
                // Set the text value with the selected date
                datePickerButton.text = dateString
                // After a date has been selected, check the inputs to enable or not the register button
                handleSignUpButtonState()
            },
            cYear,
            cMonth,
            cDay
        )
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