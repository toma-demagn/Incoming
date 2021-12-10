package com.example.tutorapp.ui.fragments

import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.tutorapp.R
import com.example.tutorapp.data.model.Ad
import com.example.tutorapp.data.network.AdRetriever
import kotlinx.android.synthetic.main.fragment_ad_creation.*
import kotlinx.coroutines.*
import java.time.LocalDateTime

/**
 * AdCreation Fragment
 */
class AdCreationFragment : Fragment() {

    // SP
    private lateinit var sp: SharedPreferences
    // Retriever
    private val adRetriever: AdRetriever = AdRetriever()
    // UI inputs
    private lateinit var titleInput: EditText
    private lateinit var descInput: EditText
    private lateinit var subjectInput: EditText
    private lateinit var locationInput: EditText
    private lateinit var levelSpinner: Spinner
    private lateinit var daySpinner: Spinner
    private lateinit var timeSpinner: Spinner

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Getting the sp values
        sp = this.requireActivity().getSharedPreferences("login", AppCompatActivity.MODE_PRIVATE)
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_ad_creation, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Init the inputs
        titleInput = adCreationFragment_titleInput
        descInput = adCreationFragment_descriptionInput
        subjectInput = adCreationFragment_subjectInput
        locationInput = adCreationFragment_locationInput
        levelSpinner = adCreationFragment_levelSpinner
        daySpinner = adCreationFragment_daySpinner
        timeSpinner = adCreationFragment_timeSpinner
        // Adding adapters to the spinners
        val levels = resources.getStringArray(R.array.EducationLevels)
        val daysOfWeek = resources.getStringArray(R.array.DaysOfWeek)
        val timeOfDays = resources.getStringArray(R.array.TimeOfDay)
        levelSpinner.adapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, levels)
        daySpinner.adapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, daysOfWeek)
        timeSpinner.adapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, timeOfDays)

        // Add listener on each edit text
        val editTextList = arrayListOf(titleInput, descInput, subjectInput, locationInput)
        for (e in editTextList) {
            e.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                override fun afterTextChanged(p0: Editable?) {
                    when {
                        titleInput.text.length < 5 -> {
                            titleInput.error = "Doit contenir au minimum 5 caractères."
                            adCreationFragment_postButton.isEnabled = false
                        }
                        descInput.text.length < 20 -> {
                            descInput.error = "Doit contenir au minimum 20 caractères."
                            adCreationFragment_postButton.isEnabled = false
                        }
                        else -> {
                            adCreationFragment_postButton.isEnabled = allInputsAreCompleted()
                        }
                    }
                }
            })
        }

        // Post button listener
        adCreationFragment_postButton.setOnClickListener {
            postAd()
        }
    }

    /**
     * Checks that each input is not empty
     */
    private fun allInputsAreCompleted(): Boolean =
        titleInput.text.isNotEmpty() && descInput.text.isNotEmpty()
                && subjectInput.text.isNotEmpty() && locationInput.text.isNotEmpty()

    /**
     * Method to post a new ad
     */
    private fun postAd() {
        val adFetchJob = Job()
        val errorHandler = CoroutineExceptionHandler { _, throwable ->
            throwable.printStackTrace()
            Toast.makeText(context, "Impossible de poster l'annonce...", Toast.LENGTH_SHORT)
                .show()
        }
        val scope = CoroutineScope(adFetchJob + Dispatchers.Main)
        scope.launch(errorHandler) {
            val availabilities = "${daySpinner.selectedItem} : ${timeSpinner.selectedItem}"
            val ad = Ad(
                isTutoringAd = adCreationFragment_tutoring.isChecked,
                title = titleInput.text.toString(),
                description = descInput.text.toString(),
                availabilities = availabilities,
                subject = subjectInput.text.toString(),
                educationLevel = levelSpinner.selectedItem.toString(),
                location = locationInput.text.toString(),
                authorId = sp.getInt("userId", -1),
                creationTime = LocalDateTime.now().toString()
            )
            adRetriever.createAd(ad)
            Toast.makeText(context, "Annonce postée.", Toast.LENGTH_LONG).show()
            // Go to home fragment
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.nav_fragment_container, HomeFragment())
            transaction.commit()
        }
    }
}