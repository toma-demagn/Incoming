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

/**
 * Search Fragment
 */
class SearchFragment : Fragment() {

    // Tag retriever
    private val tagRetriever: TagRetriever = TagRetriever()
    // Suggestions adapter
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
        // Init the recycler view
        initRecyclerView()
        // Fetching the tags
        fetchData()
        // Filter the suggestions displayed according to the search query
        sf_searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean = false
            override fun onQueryTextChange(newText: String?): Boolean {
                adapter.filter.filter(newText)
                return true
            }
        })
    }

    /**
     * Init the recycler view
     */
    private fun initRecyclerView() {
        sf_recyclerView.layoutManager = LinearLayoutManager(context)
    }

    /**
     * Gets the tags list and then display it as search suggestions
     */
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

    /**
     * Render and display the given tags list as search suggestions
     */
    private fun renderData(tagResponse: List<Tag>) {
        // Sets the progress bar visibility to gone
        sf_progressBar.visibility = View.GONE
        // When a suggestion is clicked on, we display a toast with its value
        adapter = SuggestionsAdapter(tagResponse = tagResponse) { item ->
            Toast.makeText(context, item.tag, Toast.LENGTH_SHORT).show()
        }
        // Sets the recycler view adapter and visibility
        sf_recyclerView.adapter = adapter
        sf_recyclerView.visibility = View.VISIBLE
    }

}