package com.example.tutorapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.example.tutorapp.R
import com.example.tutorapp.data.model.Tag
import kotlinx.android.synthetic.main.item_suggestion.view.*
import java.util.*
import kotlin.collections.ArrayList

/**
 * Adapter class for the suggestions list (in the search fragment)
 */
class SuggestionsAdapter(
    private val tagResponse: List<Tag>,
    private val listener: (Tag) -> Unit
) : RecyclerView.Adapter<SuggestionsAdapter.ViewHolder>(), Filterable {

    // Filtered list of items
    private var suggestionsListFiltered = ArrayList<Tag>()

    init {
        suggestionsListFiltered = tagResponse as ArrayList<Tag>
    }

    /**
     * View Holder inner class
     * Bind method to bind the data with the UI
     */
    inner class ViewHolder(view : View) : RecyclerView.ViewHolder(view){
        fun bind(tag : Tag){
            itemView.suggestion.text = tag.tag
        }
    }

    /**
     * Inflates the right layout
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(view = LayoutInflater.from(parent.context).inflate(R.layout.item_suggestion, parent, false))
    }

    /**
     * Gets an item and bind it
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val tag = suggestionsListFiltered[position]
        holder.bind(tag = tag)
        holder.itemView.setOnClickListener { listener(tag) }
    }

    /**
     * Returns the items number (equals to the list size)
     */
    override fun getItemCount(): Int = suggestionsListFiltered.size

    /**
     * Allows to filter the list and display items corresponding to the user search
     */
    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                suggestionsListFiltered = if (charSearch.isEmpty()) {
                    tagResponse as ArrayList<Tag>
                } else {
                    val resultList = ArrayList<Tag>()
                    for (item in tagResponse) {
                        if (item.tag.lowercase(Locale.ROOT).contains(charSearch.lowercase(Locale.ROOT))) {
                            resultList.add(item)
                        }
                    }
                    resultList
                }
                val filterResults = FilterResults()
                filterResults.values = suggestionsListFiltered
                return filterResults
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                suggestionsListFiltered = results?.values as ArrayList<Tag>
                notifyDataSetChanged()
            }

        }
    }
}