package com.example.tutorapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.tutorapp.R
import com.example.tutorapp.data.model.Ad
import com.example.tutorapp.data.utils.TimestampUtils
import kotlinx.android.synthetic.main.item_ad.view.*
import java.time.LocalDate

/**
 * Adapter class for an ads list
 */
class AdsAdapter(
    private val ads: List<Ad>,
    private val context: Context,
    private val listener: (Ad) -> Unit
): RecyclerView.Adapter<AdsAdapter.ViewHolder>() {

    /**
     * View Holder inner class
     * Bind method to bind the data with the UI
     */
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(ad: Ad) {
            if (!ad.isTutoringAd) itemView.adItem_imageView.setImageDrawable(
                ContextCompat.getDrawable(context, R.drawable.ic_baseline_search_24)
            )
            itemView.adItem_title.text = ad.title
            itemView.adItem_type.text = if (ad.isTutoringAd) "Tutorat" else "Recherche de tutorat"
            // Time display management
            val currentDate = LocalDate.now()
            val adCreationTimeDay = TimestampUtils.getDayOfMonth(ad.creationTime)
            val adCreationTimeYear = TimestampUtils.getYear(ad.creationTime)
            itemView.adItem_time.text = when {
                currentDate.dayOfMonth == adCreationTimeDay -> TimestampUtils.timestampToHour(ad.creationTime)
                currentDate.year == adCreationTimeYear -> TimestampUtils.timestampToDate(ad.creationTime)
                else -> TimestampUtils.timestampToDate(ad.creationTime, true)
            }
        }
    }

    /**
     * Inflates the right layout
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdsAdapter.ViewHolder {
        return ViewHolder(view = LayoutInflater.from(parent.context).inflate(R.layout.item_ad, parent, false))
    }

    /**
     * Gets an item and bind it
     */
    override fun onBindViewHolder(holder: AdsAdapter.ViewHolder, position: Int) {
        val ad = ads[position]
        holder.bind(ad = ad)
        holder.itemView.setOnClickListener{ listener(ad) }
    }

    /**
     * Returns the items number (equals to the list size)
     */
    override fun getItemCount(): Int = ads.size

}