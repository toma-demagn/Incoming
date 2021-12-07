package com.example.tutorapp.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tutorapp.R
import com.example.tutorapp.adapters.SuggestionsAdapter
import com.example.tutorapp.data.model.Tag
import com.example.tutorapp.data.network.TagRetriever
import kotlinx.android.synthetic.main.fragment_search.*
import kotlinx.coroutines.*

class SearchFragment : Fragment() {

    private val tagRetriever: TagRetriever = TagRetriever()
    private lateinit var adapter: SuggestionsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        fetchData()

        // Filter the suggestions displayed according to the search query
        sf_searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                adapter.filter.filter(newText)
                return true
            }
        })
    }

    private fun initRecyclerView() {
        sf_recyclerView.layoutManager = LinearLayoutManager(context)
    }

    private fun fetchData() {
        val tagsFetchJob = Job()

        val errorHandler = CoroutineExceptionHandler { _, throwable ->
            throwable.printStackTrace()
            Toast.makeText(context, "Impossible de récupérer les suggestions.", Toast.LENGTH_SHORT)
                .show()
        }

        val scope = CoroutineScope(tagsFetchJob + Dispatchers.Main)

        scope.launch(errorHandler) {
            val tagResponse = tagRetriever.getTags()
            renderData(tagResponse)
        }
    }

    private fun renderData(tagResponse: List<Tag>) {
        sf_progressBar.visibility = View.GONE
        adapter = SuggestionsAdapter(tagResponse = tagResponse) { item ->
            Toast.makeText(context, item.tag, Toast.LENGTH_SHORT).show()
        }
        sf_recyclerView.adapter = adapter
    }

}