package com.example.tutorapp.ui.activities

import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.tutorapp.R
import com.example.tutorapp.data.model.Login
import com.example.tutorapp.data.model.User
import com.example.tutorapp.data.network.LoginRetriever
import com.example.tutorapp.data.network.UserRetriever
import kotlinx.android.synthetic.main.activity_edit_profile.*
import kotlinx.coroutines.*

class EditProfileActivity : AppCompatActivity() {

    // Saved data
    private lateinit var sp: SharedPreferences
    private var userId: Int? = null
    // Retrievers
    private val userRetriever: UserRetriever = UserRetriever()
    private val loginRetriever: LoginRetriever = LoginRetriever()
    // Activity data
    // User object
    private lateinit var user: User
    private var login: Login = Login("", "")
    // Inputs values
    private lateinit var currentPwd: EditText
    private lateinit var newPwd: EditText
    private lateinit var confirmNewPwd: EditText
    // Values has to be saved indicator
    private var editPassword: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        // Getting the user id
        sp = getSharedPreferences("login", MODE_PRIVATE)
        userId = sp.getInt("userId", -1)
        // Getting the user data and init the switch values
        getUserData()

        // Init the edit text attributes
        currentPwd = editProfile_currentPasswordInput
        newPwd = editProfile_newPasswordInput
        confirmNewPwd = editProfile_confirmNewPasswordInput

        // Add listener on each edit text input
        val pwdEditText = arrayListOf(currentPwd, newPwd, confirmNewPwd)
        for (e in pwdEditText) {
            e.addTextChangedListener(object: TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                override fun afterTextChanged(p0: Editable?) {
                    editPassword = isPasswordLongEnough(e) && areAllPasswordsCompleted() && areThePasswordValuesRight()
                    editProfile_saveButton.isEnabled = editPassword
                }
            })
        }

        // Add listener on save button
        editProfile_saveButton.setOnClickListener {
            if (editPassword) {
                updatePassword()
            }
            finish()
        }
    }

    private fun getUserData() {
        val userFetchJob = Job()
        val errorHandler = CoroutineExceptionHandler { _, throwable ->
            throwable.printStackTrace()
            Toast.makeText(this, "Impossible de récupérer les informations du profil...", Toast.LENGTH_SHORT)
                .show()
            finish()
        }
        val scope = CoroutineScope(userFetchJob + Dispatchers.Main)
        scope.launch(errorHandler) {
            user = userRetriever.getUserById(userId!!)
            getLogin(user.email)
            initSwitchValues(user)
        }
    }

    private fun getLogin(id: String) {
        val loginFetchJob = Job()
        val scope = CoroutineScope(loginFetchJob + Dispatchers.Main)
        scope.launch{ login = loginRetriever.getLogin(id) }
    }

    private fun initSwitchValues(user: User) {
        editProfile_lookingForTutoringSwitch.isChecked = user.targetWantAds
        editProfile_tutoringAdSwitch.isChecked = user.targetTutoringAds
    }

    private fun areAllPasswordsCompleted(): Boolean =
        !(currentPwd.text.isEmpty() || newPwd.text.isEmpty() || confirmNewPwd.text.isEmpty())
    private fun isPasswordLongEnough(e: EditText): Boolean {
        return when {
            e.text.toString().length > 7 -> {
                true
            }
            e.text.toString().isNotEmpty() -> {
                e.error = "Minimum 8 caractères."
                false
            }
            else -> {
                false
            }
        }
    }
    private fun areThePasswordValuesRight(): Boolean {
        var res = true
        if (currentPwd.text.toString() != login.password) {
            currentPwd.error = "Doit être identique à votre mot de passe actuel."
            res = false
        }
        if (newPwd.text.toString() == login.password) {
            newPwd.error = "Doit être différent de votre mot de passe actuel."
            res = false
        }
        if (confirmNewPwd.text.toString() != newPwd.text.toString()) {
            confirmNewPwd.error = "Doit être identique au nouveau mot de passe renseigné."
            res = false
        }
        return res
    }

    //private fun handleSaveButtonState() {}

    private fun updatePassword() {
        val loginFetchJob = Job()
        val errorHandler = CoroutineExceptionHandler { _, throwable ->
            throwable.printStackTrace()
            Toast.makeText(this, "Impossible de mettre à jour le mot de passe...", Toast.LENGTH_SHORT)
                .show()
        }
        val scope = CoroutineScope(loginFetchJob + Dispatchers.Main)
        scope.launch(errorHandler) {
            loginRetriever.updateLogin(user.email, Login(email = user.email, password = newPwd.text.toString()))
            displayToastAfterSave()
        }
    }

    private fun displayToastAfterSave() {
        Toast.makeText(this, "Changements appliqués.", Toast.LENGTH_SHORT).show()
    }
}