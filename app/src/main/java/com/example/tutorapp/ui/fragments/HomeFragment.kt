package com.example.tutorapp.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tutorapp.R
import com.example.tutorapp.adapters.AdsAdapter
import com.example.tutorapp.data.model.Ad
import com.example.tutorapp.data.network.AdRetriever
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.coroutines.*

class HomeFragment : Fragment() {

    private val adRetriever: AdRetriever = AdRetriever()
    private lateinit var adsAdapater: AdsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(com.example.tutorapp.R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        homeFragment_recyclerView.layoutManager = LinearLayoutManager(context)
        getAds()
    }

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

    private fun renderData(ads: List<Ad>) {
        adsAdapater = AdsAdapter(ads = ads, context = requireContext()) {
            //val transaction = requireActivity().supportFragmentManager.beginTransaction()
            //transaction.replace(R.id.nav_fragment_container,
            //    AdFragment.newInstance(adId = it.id))
            //transaction.commit()
        }
        homeFragment_recyclerView.adapter = adsAdapater
        homeFragment_recyclerView.visibility = View.VISIBLE
    }
}