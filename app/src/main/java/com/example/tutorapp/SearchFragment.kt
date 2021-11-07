package com.example.tutorapp

import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment

class SearchFragment : Fragment() {

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

        // Init the adapter and pass it to the list view
        adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, resources.getStringArray(R.array.tags_list))
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