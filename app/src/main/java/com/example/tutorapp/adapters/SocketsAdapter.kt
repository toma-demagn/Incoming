package com.example.tutorapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tutorapp.R
import com.example.tutorapp.data.model.Socket
import com.example.tutorapp.data.network.UserRetriever
import com.example.tutorapp.data.utils.TimestampUtils
import kotlinx.android.synthetic.main.item_socket.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.time.LocalDate

/**
 * Adapter class for a sockets list
 */
class SocketsAdapter(
    private val sockets: List<Socket>,
    private val userId: Int,
    private val listener: (Socket) -> Unit
) : RecyclerView.Adapter<SocketsAdapter.ViewHolder>() {

    // User retriever
    private val userRetriever: UserRetriever = UserRetriever()

    /**
     * View Holder inner class
     * Bind method to bind the data with the UI
     */
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(socket: Socket) {
            val userFetchJob = Job()
            val scope = CoroutineScope(userFetchJob + Dispatchers.Main)
            scope.launch {
                // Gets the "contact" user id and then gets his username to display it
                val contactId = getContactUserId(socket)
                val user = userRetriever.getUserById(contactId)
                // Display data
                itemView.socketItem_contactTextView.text = user.username
                itemView.socketItem_lastMessageTextView.text = socket.lastMessage
                // Time display management
                val currentDate = LocalDate.now()
                val socketLastUpdateDay = TimestampUtils.getDayOfMonth(socket.lastUpdate)
                val socketLastUpdateYear = TimestampUtils.getYear(socket.lastUpdate)
                itemView.socketItem_timeTextView.text = when {
                    currentDate.dayOfMonth == socketLastUpdateDay -> {
                        TimestampUtils.timestampToHour(socket.lastUpdate)
                    }
                    currentDate.year == socketLastUpdateYear -> {
                        TimestampUtils.timestampToDate(socket.lastUpdate)
                    }
                    else -> {
                        TimestampUtils.timestampToDate(socket.lastUpdate, true)
                    }
                }
            }
        }
    }

    /**
     * Inflates the right layout
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SocketsAdapter.ViewHolder {
        return ViewHolder(view = LayoutInflater.from(parent.context).inflate(R.layout.item_socket, parent, false))
    }

    /**
     * Gets an item and bind it
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val socket = sockets[position]
        holder.bind(socket = socket)
        holder.itemView.setOnClickListener{ listener(socket) }
    }

    /**
     * Returns the items number (equals to the list size)
     */
    override fun getItemCount(): Int = sockets.size

    /**
     * Gets the userId of the other user (named "contact" here)
     * Useful to get his username then
     */
    private fun getContactUserId(socket: Socket): Int {
        return if (userId == socket.fromId) {
            socket.toId
        } else {
            socket.fromId
        }
    }

}