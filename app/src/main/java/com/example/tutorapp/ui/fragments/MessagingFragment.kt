package com.example.tutorapp.ui.fragments

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tutorapp.R
import com.example.tutorapp.adapters.SocketsAdapter
import com.example.tutorapp.data.model.Socket
import com.example.tutorapp.data.network.SocketRetriever
import kotlinx.android.synthetic.main.fragment_messaging.*
import kotlinx.coroutines.*

class MessagingFragment : Fragment() {

    private lateinit var sp: SharedPreferences
    private val socketRetriever: SocketRetriever = SocketRetriever()
    private lateinit var socketsAdapter: SocketsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Getting the sp values
        sp = this.requireActivity().getSharedPreferences("login", AppCompatActivity.MODE_PRIVATE)
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_messaging, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        getUserSockets()
    }

    private fun initRecyclerView() {
        mf_socketsRecyclerView.layoutManager = LinearLayoutManager(context)
    }

    private fun getUserSockets() {
        val socketsFetchJob = Job()

        val errorHandler = CoroutineExceptionHandler { _, throwable ->
            throwable.printStackTrace()
            Toast.makeText(context, "Impossible de récupérer les messages...", Toast.LENGTH_SHORT)
                .show()
        }

        val scope = CoroutineScope(socketsFetchJob + Dispatchers.Main)
        scope.launch(errorHandler) {
            val userId = sp.getInt("userId", -1)
            val sockets = socketRetriever.getSocketsByUserId(userId)
            renderData(sockets, userId)
        }
    }

    private fun renderData(sockets: List<Socket>, userId: Int) {
        socketsAdapter = SocketsAdapter(sockets = sockets, userId = userId) { socket ->
            Toast.makeText(context, socket.lastUpdate, Toast.LENGTH_SHORT).show()
        }
        mf_socketsRecyclerView.adapter = socketsAdapter
        mf_progressBar.visibility = View.GONE
        mf_socketsRecyclerView.visibility = View.VISIBLE
    }


}