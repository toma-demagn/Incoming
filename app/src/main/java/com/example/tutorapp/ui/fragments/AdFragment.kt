package com.example.tutorapp.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.tutorapp.R
import com.example.tutorapp.data.model.Ad
import com.example.tutorapp.data.model.Socket
import com.example.tutorapp.data.network.AdRetriever
import com.example.tutorapp.data.network.SocketRetriever
import com.example.tutorapp.data.network.UserRetriever
import com.example.tutorapp.data.utils.TimestampUtils
import kotlinx.android.synthetic.main.fragment_ad.*
import kotlinx.coroutines.*
import java.time.LocalDateTime

private const val AD_ID = "adId"
private const val USER_ID = "userId"

class AdFragment : Fragment() {

    // Parameters
    private var adId: Int? = null
    private var userId: Int? = null
    // Ad object
    private lateinit var ad: Ad
    // Retrievers
    private val adRetriever: AdRetriever = AdRetriever()
    private val socketRetriever: SocketRetriever = SocketRetriever()
    private val userRetriever: UserRetriever = UserRetriever()

    companion object {
        @JvmStatic
        fun newInstance(adId: Int, userId: Int) =
            AdFragment().apply {
                arguments = Bundle().apply {
                    putInt(AD_ID, adId)
                    putInt(USER_ID, userId)
                }
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            adId = it.getInt(AD_ID)
            userId = it.getInt(USER_ID)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_ad, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initData()
        adFragment_contactButton.setOnClickListener {
            contactAuthor()
        }
    }

    private fun initData() {
        val adFetchJob = Job()
        val errorHandler = CoroutineExceptionHandler { _, throwable ->
            throwable.printStackTrace()
            Toast.makeText(context, "Impossible de récupérer les informations de l'annonce...", Toast.LENGTH_SHORT)
                .show()

        }
        val scope = CoroutineScope(adFetchJob + Dispatchers.Main)
        scope.launch(errorHandler) {
            ad = adRetriever.getAdById(adId!!)
            initUI(ad, errorHandler)
        }
    }

    private fun initUI(ad: Ad, errorHandler: CoroutineExceptionHandler) {
        val userFetchJob = Job()
        val scope = CoroutineScope(userFetchJob + Dispatchers.Main)
        scope.launch(errorHandler) {
            val authorUsername = userRetriever.getUserById(ad.authorId).username
            if (ad.isTutoringAd) adFragment_imageView.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_search_24))
            adFragment_title.text = ad.title
            adFragment_description.text = ad.description
            adFragment_type.text = if (ad.isTutoringAd) "Tutorat" else "Recherche de tutorat"
            adFragment_subject.text = ad.subject
            adFragment_level.text = ad.educationLevel
            adFragment_availabilities.text = ad.availabilities
            adFragment_location.text = ad.location
            adFragment_author.text = authorUsername
            adFragment_creationTime.text = TimestampUtils.timestampToDate(ad.creationTime, true)
            adFragment_contactButton.isEnabled = ad.authorId != userId
        }
    }

    private fun contactAuthor() {
        val socketFetchJob = Job()
        val errorHandler = CoroutineExceptionHandler { _, throwable ->
            throwable.printStackTrace()
            Toast.makeText(context, "Impossible de contacter l'auteur...", Toast.LENGTH_SHORT)
                .show()
        }
        val scope = CoroutineScope(socketFetchJob + Dispatchers.Main)
        scope.launch(errorHandler) {
            val socket = socketRetriever.createSocket(Socket(
                fromId = userId!!,
                toId = ad.authorId,
                creationTime = LocalDateTime.now().toString(),
                lastUpdate = LocalDateTime.now().toString()
            ))
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(
                R.id.nav_fragment_container,
                ChatFragment.newInstance(socketId = socket.id!!, userId = userId!!)
            )
            transaction.commit()
        }
    }
}