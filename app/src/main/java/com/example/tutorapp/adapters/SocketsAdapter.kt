package com.example.tutorapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tutorapp.R
import com.example.tutorapp.data.model.Socket
import kotlinx.android.synthetic.main.socket_item.view.*

class SocketsAdapter(
    private val sockets: List<Socket>,
    private val userId: Int,
    private val listener: (Socket) -> Unit
) : RecyclerView.Adapter<SocketsAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(socket: Socket) {
            itemView.socketItem_contactTextView.text = getContactUserId(socket).toString()
            itemView.socketItem_lastMessageTextView.text = socket.lastMessage
            // TODO : time to day/hour
            itemView.socketItem_timeTextView.text = socket.lastUpdate.split('T')[0]
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SocketsAdapter.ViewHolder {
        return ViewHolder(view = LayoutInflater.from(parent.context).inflate(R.layout.socket_item, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val socket = sockets[position]
        holder.bind(socket = socket)
        holder.itemView.setOnClickListener{ listener(socket) }
    }

    override fun getItemCount(): Int = sockets.size

    private fun getContactUserId(socket: Socket): Int {
        return if (userId == socket.fromId) {
            socket.toId
        } else {
            socket.fromId
        }
    }

}