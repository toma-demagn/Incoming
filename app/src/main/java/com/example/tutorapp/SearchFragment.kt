package com.example.tutorapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import org.json.JSONArray

class SearchFragment : Fragment() {

    private val jsonStringTags = """
        [
            {"tag":"Mathématiques", "category":"Cours"},
            {"tag":"Cuisine", "category":"Cours"},
            {"tag":"Français", "category":"Cours"},
            {"tag":"Chimie", "category":"Cours"},
            {"tag":"Physique", "category":"Cours"},
            {"tag":"Lycée", "category":"Niveau"},
            {"tag":"Terminale", "category":"Niveau"},
            {"tag":"Première", "category":"Niveau"},
            {"tag":"Seconde", "category":"Niveau"},
            {"tag":"Collège", "category":"Niveau"},
            {"tag":"3eme", "category":"Niveau"},
            {"tag":"4eme", "category":"Niveau"},
            {"tag":"5eme", "category":"Niveau"},
            {"tag":"6eme", "category":"Niveau"}
        ]
        """

    private lateinit var viewOfLayout: View
    private lateinit var adapter: ArrayAdapter<*>

    companion object {
        fun newInstance(): SearchFragment {
            return SearchFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        viewOfLayout = inflater.inflate(R.layout.fragment_search, container, false)

        // Get the layout views
        val searchView = viewOfLayout.findViewById<SearchView>(R.id.sf_searchView)
        val lv = viewOfLayout.findViewById<ListView>(R.id.sf_listView)
        lv.emptyView = viewOfLayout.findViewById<TextView>(R.id.sf_emptyTextView)

        // Get JSON object and adding the tags values in a list
        val tagsList: MutableList<String> = mutableListOf()
        val jsonArray = JSONArray(jsonStringTags)
        for (i in 0 until jsonArray.length()) {
            tagsList.add(jsonArray.getJSONObject(i).getString("tag"))
        }
        // Adding an empty item to fix the list view bottom position issue in the layout
        tagsList.add("")

        // Init the adapter and pass it to the list view
        adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, tagsList)
        lv.adapter = adapter
        lv.onItemClickListener = AdapterView.OnItemClickListener { parent, v, position, id ->
            Toast.makeText(context, parent?.getItemAtPosition(position).toString(), Toast.LENGTH_SHORT).show()
        }

        // Handle the search query
        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                adapter.filter.filter(newText)
                return true
            }
        })

        return viewOfLayout
    }

}