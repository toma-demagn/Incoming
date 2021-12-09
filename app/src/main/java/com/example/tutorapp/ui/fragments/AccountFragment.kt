package com.example.tutorapp.ui.fragments

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.tutorapp.R
import com.example.tutorapp.data.model.User
import com.example.tutorapp.data.network.UserRetriever
import com.example.tutorapp.data.utils.TimestampUtils
import com.example.tutorapp.ui.activities.EditProfileActivity
import com.example.tutorapp.ui.activities.SignUpActivity
import kotlinx.android.synthetic.main.fragment_account.*
import kotlinx.coroutines.*

private const val USER_ID = "userId"
private const val REQUEST_CODE_TO_EDIT = 1

class AccountFragment : Fragment() {

    private lateinit var sp: SharedPreferences
    private var userId: Int? = null
    private val userRetriever: UserRetriever = UserRetriever()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        sp = this.requireActivity().getSharedPreferences("login", AppCompatActivity.MODE_PRIVATE)
        userId = sp.getInt(USER_ID, -1)
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_account, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Getting the user data and update the UI
        getUserData()
        // Setting listeners for the buttons
        accountFragment_editButton.setOnClickListener{ goToEdit() }
        accountFragment_logoutButton.setOnClickListener { logOut() }

    }

    private fun getUserData() {
        val userFetchJob = Job()
        val errorHandler = CoroutineExceptionHandler { _, throwable ->
            throwable.printStackTrace()
            Toast.makeText(context, "Impossible de récupérer les informations du profil...", Toast.LENGTH_SHORT)
                .show()
        }
        val scope = CoroutineScope(userFetchJob + Dispatchers.Main)
        scope.launch(errorHandler) {
            val user = userRetriever.getUserById(userId!!)
            initUIWithUserData(user)
        }
    }

    private fun initUIWithUserData(user: User) {
        accountFragment_usernameTextView.text = user.username
        accountFragment_bioTextView.text =
            if (user.bio.isEmpty()) resources.getString(R.string.empty) else user.bio
        val names = "${user.firstName} ${user.lastName}"
        accountFragment_namesTextView.text = names
        accountFragment_emailTextView.text = user.email
        accountFragment_birthdateTextView.text = TimestampUtils.timestampToDate(user.birthDate, true)
    }

    private fun goToEdit() {
        val intent = Intent(context, EditProfileActivity::class.java)
        startActivityForResult(intent, REQUEST_CODE_TO_EDIT)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_TO_EDIT && resultCode == Activity.RESULT_OK) {
            getUserData()
        }
    }

    private fun logOut() {
        sp.edit().putBoolean("isLoggedIn", false).apply()
        sp.edit().putInt("userId", -1).apply()
        val signUpActivityIntent = Intent(context, SignUpActivity::class.java)
        signUpActivityIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(signUpActivityIntent)
    }
}