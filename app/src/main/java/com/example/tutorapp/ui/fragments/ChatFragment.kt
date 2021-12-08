package com.example.tutorapp.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.tutorapp.R
import com.example.tutorapp.data.model.Socket
import com.example.tutorapp.data.network.SocketRetriever
import com.example.tutorapp.data.network.UserRetriever
import kotlinx.android.synthetic.main.fragment_chat.*
import kotlinx.coroutines.*

private const val SOCKET_ID = "socketId"
private const val USER_ID = "userId"

class ChatFragment : Fragment() {

    // Data parameters
    private var socketId: Int? = null
    private var userId: Int? = null

    // Retrievers
    private val socketRetriever: SocketRetriever = SocketRetriever()
    private val userRetriever: UserRetriever = UserRetriever()

    // Data
    private lateinit var socket: Socket

    companion object {
        @JvmStatic
        fun newInstance(socketId: Int, userId: Int) =
            ChatFragment().apply {
                arguments = Bundle().apply {
                    putInt(SOCKET_ID, socketId)
                    putInt(USER_ID, userId)
                }
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Getting parameters
        arguments?.let {
            socketId = it.getInt(SOCKET_ID)
            userId = it.getInt(USER_ID)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chat, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Getting data from API
        initData()
        // Back button management
        chatFragment_backButton.setOnClickListener {
            goBackToSocketsFragment()
        }
    }

    private fun initData() {
        val socketFetchJob = Job()
        val errorHandler = CoroutineExceptionHandler { _, throwable ->
            throwable.printStackTrace()
            Toast.makeText(context, "Impossible de récupérer les messages...", Toast.LENGTH_SHORT)
                .show()
        }
        val scope = CoroutineScope(socketFetchJob + Dispatchers.Main)
        scope.launch(errorHandler) {
            socket = socketRetriever.getSocketById(socketId!!)
            val contactUserId = if (socket.fromId == userId) socket.toId else socket.fromId
            setContactUsername(contactUserId)
        }
    }

    private fun setContactUsername(contactUserId: Int) {
        val userFetchJob = Job()
        val scope = CoroutineScope(userFetchJob + Dispatchers.Main)
        scope.launch {
            val contactUser = userRetriever.getUserById(contactUserId)
            chatFragment_contactUsername.text = contactUser.username
        }
    }

    private fun goBackToSocketsFragment() {
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.nav_fragment_container, SocketsFragment())
        transaction.commit()
    }
}