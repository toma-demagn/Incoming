package com.example.tutorapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tutorapp.R
import com.example.tutorapp.data.model.Message
import com.example.tutorapp.data.utils.TimestampUtils
import kotlinx.android.synthetic.main.item_chat_contact.view.*
import kotlinx.android.synthetic.main.item_chat_user.view.*

private const val VIEW_TYPE_MESSAGE_FROM_USER = 1
private const val VIEW_TYPE_MESSAGE_FROM_CONTACT = 2

class MessagesAdapter(
    private val messages: List<Message>,
    private val userIsSocketAuthor: Boolean,
    private val contactUsername: String
): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun getItemViewType(position: Int): Int =
        if (messages[position].isFromSender && userIsSocketAuthor) {
            VIEW_TYPE_MESSAGE_FROM_USER
        } else {
            VIEW_TYPE_MESSAGE_FROM_CONTACT
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        if (viewType == VIEW_TYPE_MESSAGE_FROM_USER) {
            SentMessageHolder(view = LayoutInflater.from(parent.context).inflate(R.layout.item_chat_user, parent, false))
        } else {
            ReceivedMessageHolder(view = LayoutInflater.from(parent.context).inflate(R.layout.item_chat_contact, parent, false))
        }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message = messages[position]
        when (holder.itemViewType) {
            VIEW_TYPE_MESSAGE_FROM_USER -> (holder as SentMessageHolder).bind(message)
            VIEW_TYPE_MESSAGE_FROM_CONTACT -> (holder as ReceivedMessageHolder).bind(message)
        }
    }

    override fun getItemCount(): Int = messages.size

    inner class SentMessageHolder(view : View) : RecyclerView.ViewHolder(view){
        fun bind(message: Message){
            itemView.itemChatUser_dateTextView.text = TimestampUtils.timestampToDate(message.time)
            itemView.itemChatUser_messageTextView.text = message.content
            itemView.itemChatUser_hourTextView.text = TimestampUtils.timestampToHour(message.time)
        }
    }

    inner class ReceivedMessageHolder(view : View) : RecyclerView.ViewHolder(view){
        fun bind(message: Message){
            itemView.itemChatContact_dateTextView.text = TimestampUtils.timestampToDate(message.time)
            itemView.itemChatContact_messageTextView.text = message.content
            itemView.itemChatContact_hourTextView.text = TimestampUtils.timestampToHour(message.time)
        }
    }
}