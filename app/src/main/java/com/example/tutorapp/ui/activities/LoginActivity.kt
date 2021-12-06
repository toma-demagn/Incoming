package com.example.tutorapp.ui.activities

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.tutorapp.MainActivity
import com.example.tutorapp.R
import com.example.tutorapp.data.model.Login
import com.example.tutorapp.data.network.LoginRetriever
import com.example.tutorapp.databinding.ActivityLoginBinding
import kotlinx.coroutines.*

class LoginActivity : AppCompatActivity() {

    // Login data retriever
    private val loginRetriever: LoginRetriever = LoginRetriever()

    // UI binding
    private lateinit var binding: ActivityLoginBinding

    // Edit text values
    private var emailValue: String = ""
    private var passwordValue: String = ""

    // SharedPreferences (saved data in the app)
    private lateinit var sp: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        sp = getSharedPreferences("login", MODE_PRIVATE)
        if (sp.getBoolean("isLoggedIn", false)) {

        }

        // Init the edit text inputs
        val emailEditText: EditText = binding.loginEmailEditText
        val passwordEditText: EditText = binding.loginPasswordEditText

        val editTextList: ArrayList<EditText> = arrayListOf(emailEditText, passwordEditText)

        for (e in editTextList) {
            e.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                override fun afterTextChanged(p0: Editable?) {
                    handleTextChanged(emailEditText, passwordEditText)
                }
            })
        }
    }

    /**
     * Check the inputs values and set the button status
     */
    private fun handleTextChanged(emailEditText: EditText, passwordEditText: EditText) {
        val signInButton = findViewById<Button>(R.id.login_signInButton)
        emailValue = emailEditText.text.toString()
        passwordValue = passwordEditText.text.toString()
        signInButton.isEnabled = !(emailValue.isEmpty() || passwordValue.isEmpty())
    }

    /**
     * Triggered when the sign in button is clicked on.
     * Go to the main activity and sign in the user if the values are right.
     */
    fun signIn(view: View) {
        val loginFetchJob = Job()

        val errorHandler = CoroutineExceptionHandler { _, throwable ->
            throwable.printStackTrace()
            Toast.makeText(this, "Cette adresse email n'est pas reconnue.", Toast.LENGTH_SHORT)
                .show()
        }

        val scope = CoroutineScope(loginFetchJob + Dispatchers.Main)

        scope.launch(errorHandler) {
            // Get the login from the DB
            val login = loginRetriever.getLogin(emailValue)
            // If the inputs values are right, we go to the main activity
            if (isLoginOk(login)) {
                sp.edit().putBoolean("isLoggedIn", true).apply()
                // TODO : put the user id in the sp
                goToMainActivity()
            } else {
                Toast.makeText(
                    this@LoginActivity,
                    "Adresse email et/ou mot de passe erroné(s).",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    /**
     * Check if the edit text values are equals to the login object values
     * Return true if it is the case, false otherwise
     */
    private fun isLoginOk(login: Login): Boolean {
        return login.email == emailValue && login.password == passwordValue
    }

    /**
     * Go to the main activity
     */
    private fun goToMainActivity() {
        val mainActivityIntent = Intent(this@LoginActivity, MainActivity::class.java)
        mainActivityIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(mainActivityIntent)
    }

    /**
     * Go back to the SignUp Activity when the user clicks on the "create account" button
     */
    fun goBackToSignUpActivity(view: View) {
        finish()
    }
}