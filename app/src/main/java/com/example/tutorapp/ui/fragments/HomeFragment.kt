package com.example.tutorapp.ui.fragments

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tutorapp.R
import com.example.tutorapp.adapters.AdsAdapter
import com.example.tutorapp.data.model.Ad
import com.example.tutorapp.data.network.AdRetriever
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.coroutines.*

/**
 * Home Fragment
 */
class HomeFragment : Fragment() {

    // Saved data
    private lateinit var sp: SharedPreferences
    // Ad retriever
    private val adRetriever: AdRetriever = AdRetriever()
    // Ads adapter
    private lateinit var adsAdapater: AdsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Getting the sp values
        sp = this.requireActivity().getSharedPreferences("login", AppCompatActivity.MODE_PRIVATE)
        // Inflate the layout for this fragment
        return inflater.inflate(com.example.tutorapp.R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Init the recycler view
        homeFragment_recyclerView.layoutManager = LinearLayoutManager(context)
        // Gets the ads list
        getAds()
    }

    /**
     * Gets the ads list and then display it
     */
    private fun getAds() {
        val adsFetchJob = Job()
        val errorHandler = CoroutineExceptionHandler { _, throwable ->
            throwable.printStackTrace()
            Toast.makeText(context, "Impossible de récupérer les annonces...", Toast.LENGTH_SHORT)
                .show()
        }
        val scope = CoroutineScope(adsFetchJob + Dispatchers.Main)
        scope.launch(errorHandler) {
            val ads = adRetriever.getAds().sortedByDescending { it.creationTime }
            homeFragment_progressBar.visibility = View.GONE
            if (ads.isNotEmpty()) {
                renderData(ads)
            }
        }
    }

    /**
     * Render and display the ads list
     */
    private fun renderData(ads: List<Ad>) {
        // If an ad is clicked on, we go to the ad fragment
        adsAdapater = AdsAdapter(ads = ads, context = requireContext()) {
            val userId = sp.getInt("userId", -1)
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(
                R.id.nav_fragment_container,
                AdFragment.newInstance(adId = it.id!!, userId = userId))
            transaction.commit()
        }
        // Updates the recycler view adapter and visibility
        homeFragment_recyclerView.adapter = adsAdapater
        homeFragment_recyclerView.visibility = View.VISIBLE
    }
}