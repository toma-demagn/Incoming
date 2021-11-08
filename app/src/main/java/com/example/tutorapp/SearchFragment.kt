package com.example.tutorapp

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import org.json.JSONObject

class SearchFragment : Fragment() {

    private val jsonStringTags = """{"tag":"Mathématiques", "category":"C   ours"}"""

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
        val jsonObject = JSONObject(jsonStringTags)
        val tagsList: MutableList<String> = mutableListOf(jsonObject.getString("tag"))

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