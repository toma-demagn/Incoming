package com.example.tutorapp

import android.app.DatePickerDialog
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.tutorapp.data.model.Login
import com.example.tutorapp.data.model.User
import com.example.tutorapp.data.network.LoginRetriever
import com.example.tutorapp.data.network.UserRetriever
import com.example.tutorapp.ui.activities.LoginActivity
import kotlinx.coroutines.*
import java.util.*

class SignUpActivity : AppCompatActivity() {

    // Data retrievers
    private val loginRetriever: LoginRetriever = LoginRetriever()
    private val userRetriever: UserRetriever = UserRetriever()

    // UI Components
    private lateinit var firstNameEditText: EditText
    private lateinit var lastNameEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var usernameEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var confirmPasswordEditText: EditText

    private lateinit var editTextList: ArrayList<EditText>

    // Activity data
    private var birthDateValue = ""

    // SharedPreferences (saved data in the app)
    private lateinit var sp: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        Locale.setDefault(Locale.FRANCE)

        // If the user is already logged in, we go to the main activity
        sp = getSharedPreferences("login", MODE_PRIVATE)
        if (sp.getBoolean("isLoggedIn", false)) {
            goToMainActivity()
        }

        // Init the edit text inputs
        firstNameEditText = findViewById(R.id.signUp_firstNameEditText)
        lastNameEditText = findViewById(R.id.signUp_lastNameEditText)
        emailEditText = findViewById(R.id.signUp_emailEditText)
        usernameEditText = findViewById(R.id.signUp_usernameEditText)
        passwordEditText = findViewById(R.id.signUp_passwordEditText)
        confirmPasswordEditText = findViewById(R.id.signUp_confirmPasswordEditText)

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
        if (allFieldsCompleted && birthDateValue.isEmpty()) {
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
        val passwordValue: String = passwordEditText.text.toString()
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
        val isUsernameLongEnough = usernameEditText.text.toString().length > 2
        if (!isUsernameLongEnough) {
            usernameEditText.error = "Le nom d'utilisateur doit au minimum contenir 3 caractères."
        }
        // Password
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
            android.R.style.Theme_Material_Dialog_Alert, { _, year, month, day ->
                val dayString = if (day < 10) "0$day" else "$day"
                val monthNumber = month + 1
                val monthString = if (monthNumber < 10) "0$monthNumber" else "$monthNumber"
                // Date to String
                birthDateValue = "$year-$monthString-$dayString"
                val dateString = "${getString(R.string.dateOfBirth)} : $dayString/$monthString/$year"
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
     * Method to create a new login and user account, then go to the MainActivity
     */
    fun createAccount(view: View) {
        val loginFetchJob = Job()

        val errorHandler = CoroutineExceptionHandler { _, throwable ->
            throwable.printStackTrace()
            emailEditText.error = "Cette adresse email est déjà utilisée."
        }

        val scope = CoroutineScope(loginFetchJob + Dispatchers.Main)

        scope.launch(errorHandler) {
            // Save the login in DB
            val emailValue: String = emailEditText.text.toString()
            val passwordValue: String = passwordEditText.text.toString()
            val login = Login(emailValue, passwordValue)
            loginRetriever.createLogin(login)
            // Save the user in DB
            createUser()
        }
    }

    /**
     * Create a user account
     */
    private fun createUser() {
        val userFetchJob = Job()
        val emailValue: String = emailEditText.text.toString()

        val errorHandler = CoroutineExceptionHandler { _, throwable ->
            throwable.printStackTrace()
            usernameEditText.error = "Ce nom d'utilisateur est déjà utilisé."
            deleteLogin(emailValue)
        }

        val scope = CoroutineScope(userFetchJob + Dispatchers.Main)

        scope.launch(errorHandler) {
            val firstNameValue: String = firstNameEditText.text.toString()
            val lastNameValue: String = lastNameEditText.text.toString()
            val usernameValue: String = usernameEditText.text.toString()
            val user = User(
                firstName = firstNameValue,
                lastName = lastNameValue,
                email = emailValue,
                username = usernameValue,
                birthDate = birthDateValue
            )
            // We save the useful data in the SharedPreferences
            val createdUser = userRetriever.createUser(user)
            sp.edit().putBoolean("isLoggedIn", true).apply()
            sp.edit().putInt("userId", createdUser.id!!).apply()

            // If the user is well saved in the DB, we can go to the main activity
            goToMainActivity()
        }
    }

    /**
     * Delete a login if the user cannot be created
     */
    private fun deleteLogin(emailValue: String) {
        val loginFetchJob = Job()

        val errorHandler = CoroutineExceptionHandler { _, throwable ->
            throwable.printStackTrace()
        }

        val scope = CoroutineScope(loginFetchJob + Dispatchers.Main)
        scope.launch(errorHandler) {
            loginRetriever.deleteLogin(emailValue)
        }
    }

    /**
     * onClick method of the signInButton
     * Method to go to the LoginActivity
     */
    fun goToLoginActivity(view: View) {
        val loginIntent = Intent(this, LoginActivity::class.java)
        startActivity(loginIntent)
    }

    /**
     * Method to go to the main activity
     */
    private fun goToMainActivity() {
        val mainActivityIntent = Intent(this, MainActivity::class.java)
        mainActivityIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(mainActivityIntent)
    }
}