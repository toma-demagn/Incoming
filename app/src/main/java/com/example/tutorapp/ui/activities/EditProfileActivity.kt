package com.example.tutorapp.ui.activities

import android.app.Activity
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
import com.google.android.material.switchmaterial.SwitchMaterial
import kotlinx.android.synthetic.main.activity_edit_profile.*
import kotlinx.coroutines.*

/**
 * EditProfile Activity
 */
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
    // Inputs
    private lateinit var currentPwd: EditText
    private lateinit var newPwd: EditText
    private lateinit var confirmNewPwd: EditText
    private lateinit var bio: EditText
    private lateinit var firstName: EditText
    private lateinit var lastName: EditText
    private lateinit var lookingForTutoring: SwitchMaterial
    private lateinit var tutoringAds: SwitchMaterial
    // Booleans to know which values have to be updated
    private var editPassword = false
    private var editBio = false
    private var editFirstName = false
    private var editLastName = false
    private var editTargetWantAds = false
    private var editTargetTutoringAds = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        // Getting the user id
        sp = getSharedPreferences("login", MODE_PRIVATE)
        userId = sp.getInt("userId", -1)
        // Init the inputs
        currentPwd = editProfile_currentPasswordInput
        newPwd = editProfile_newPasswordInput
        confirmNewPwd = editProfile_confirmNewPasswordInput
        bio = editProfile_bioInput
        firstName = editProfile_firstNameInput
        lastName = editProfile_lastNameInput
        lookingForTutoring = editProfile_lookingForTutoringSwitch
        tutoringAds = editProfile_tutoringAdSwitch
        // Getting the user data and init the switch values
        getUserData()

        // Add listener on each edit text input
        val pwdEditText = arrayListOf(currentPwd, newPwd, confirmNewPwd)
        val allEditText = arrayListOf(currentPwd, newPwd, confirmNewPwd, bio, firstName, lastName)
        for (e in allEditText) {
            e.addTextChangedListener(object: TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                override fun afterTextChanged(p0: Editable?) {
                    val nameError = "Ne peut pas être nul."
                    if (pwdEditText.contains(e)) {
                        editPassword = isPasswordLongEnough(e) && areAllPasswordsCompleted() && areThePasswordValuesRight()
                    } else if (e == bio) {
                        editBio = bio.text.toString() != user.bio
                    } else if (e == firstName) {
                        editFirstName = firstName.text.toString() != user.firstName &&
                                firstName.text.toString().isNotEmpty()
                        if (firstName.text.toString().isEmpty()) firstName.error = nameError
                    } else {
                        editLastName = lastName.text.toString() != user.lastName &&
                                lastName.text.toString().isNotEmpty()
                        if (lastName.text.toString().isEmpty()) lastName.error = nameError
                    }
                    handleSaveButtonState()
                }
            })
        }

        // Add listener on each switch
        lookingForTutoring.setOnCheckedChangeListener{ _, _ ->
            editTargetWantAds = lookingForTutoring.isChecked != user.targetWantAds
            handleSaveButtonState()
        }

        tutoringAds.setOnCheckedChangeListener{ _, _ ->
            editTargetTutoringAds = tutoringAds.isChecked != user.targetTutoringAds
            handleSaveButtonState()
        }

        // Add listener on save button
        editProfile_saveButton.setOnClickListener {
            if (editPassword) {
                updatePassword()
            }
            if (editBio || editFirstName || editLastName || editTargetWantAds || editTargetTutoringAds) {
                updateUser()
            }
            Toast.makeText(this, "Changements appliqués.", Toast.LENGTH_SHORT).show()
            setResult(Activity.RESULT_OK)
            finish()
        }

        // Add listener on cancel button
        editProfile_cancelButton.setOnClickListener { finish() }
    }

    /* INIT METHODS */

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
            initUIWithUserData(user)
        }
    }

    private fun getLogin(id: String) {
        val loginFetchJob = Job()
        val scope = CoroutineScope(loginFetchJob + Dispatchers.Main)
        scope.launch{ login = loginRetriever.getLogin(id) }
    }

    private fun initUIWithUserData(user: User) {
        bio.setText(user.bio)
        firstName.setText(user.firstName)
        lastName.setText(user.lastName)
        lookingForTutoring.isChecked = user.targetWantAds
        tutoringAds.isChecked = user.targetTutoringAds
    }

    /* INPUTS CONTROL FUNCTIONS */

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

    private fun handleSaveButtonState() {
        editProfile_saveButton.isEnabled = editPassword || editBio || editFirstName
                || editLastName || editTargetWantAds || editTargetTutoringAds
    }

    /* UPDATE METHODS */

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
        }
    }

    private fun updateUser() {
        val userFetchJob = Job()
        val errorHandler = CoroutineExceptionHandler { _, throwable ->
            throwable.printStackTrace()
            Toast.makeText(this, "Impossible de mettre à jour les informations...", Toast.LENGTH_SHORT)
                .show()
        }
        val scope = CoroutineScope(userFetchJob + Dispatchers.Main)
        scope.launch(errorHandler) {
            val updatedUser = User(
                firstName = if (editFirstName) firstName.text.toString() else user.firstName,
                lastName = if (editLastName) lastName.text.toString() else user.lastName,
                email = user.email,
                username = user.username,
                birthDate = user.birthDate,
                bio = if (editBio) bio.text.toString() else user.bio,
                targetWantAds = if (editTargetWantAds) lookingForTutoring.isChecked else user.targetWantAds,
                targetTutoringAds = if (editTargetTutoringAds) tutoringAds.isChecked else user.targetTutoringAds
            )
            userRetriever.updateUser(userId!!, updatedUser)
        }
    }
}