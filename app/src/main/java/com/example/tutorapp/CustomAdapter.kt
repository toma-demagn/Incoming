package com.example.tutorapp

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.*
class CustomAdapter(private val context: Activity, private val title: Array<String>, private val description: Array<String>, private val imgid: Array<Int>)
    : ArrayAdapter<String>(context, R.layout.model, title) {

    override fun getView(position: Int, view: View?, parent: ViewGroup): View {
        val inflater = context.layoutInflater
        val rowView = inflater.inflate(R.layout.model, null, true)

        val titleText = rowView.findViewById(R.id.nameTxt) as TextView
        val imageView = rowView.findViewById(R.id.spacecraftImg) as ImageView
        val subtitleText = rowView.findViewById(R.id.propellantTxt) as TextView

        titleText.text = title[position]
        imageView.setImageResource(imgid[position])
        subtitleText.text = description[position]

        return rowView
    }
}