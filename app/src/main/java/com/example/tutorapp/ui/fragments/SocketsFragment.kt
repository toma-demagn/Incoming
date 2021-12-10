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
import kotlinx.android.synthetic.main.fragment_sockets.*
import kotlinx.coroutines.*

/**
 * Sockets Fragment
 */
class SocketsFragment : Fragment() {

    // Saved data
    private lateinit var sp: SharedPreferences
    // Socket retriever
    private val socketRetriever: SocketRetriever = SocketRetriever()
    // Sockets adapter
    private lateinit var socketsAdapter: SocketsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Getting the sp values
        sp = this.requireActivity().getSharedPreferences("login", AppCompatActivity.MODE_PRIVATE)
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sockets, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Init the recycler view
        initRecyclerView()
        // Gets the user sockets
        getUserSockets()
    }

    /**
     * Init the recycler view
     */
    private fun initRecyclerView() {
        socketsFragment_socketsRecyclerView.layoutManager = LinearLayoutManager(context)
    }

    /**
     * Gets the sockets of the current user
     */
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
            val sockets = socketRetriever.getSocketsByUserId(userId).sortedByDescending { it.lastUpdate }
            socketsFragment_progressBar.visibility = View.GONE
            if (sockets.isNotEmpty()) {
                // We render the fetched data
                renderData(sockets, userId)
            } else {
                // If there is no existing socket for this user,
                // we display a text view to inform him.
                socketsFragment_emptySocketsTextView.visibility = View.VISIBLE
            }
        }
    }

    /**
     * Render and display the sockets
     */
    private fun renderData(sockets: List<Socket>, userId: Int) {
        // If a socket is clicked on, we go to the chat fragment
        socketsAdapter = SocketsAdapter(sockets = sockets, userId = userId) {
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.nav_fragment_container,
                ChatFragment.newInstance(socketId = it.id!!, userId = userId))
            transaction.commit()
        }
        // Sets the recycler view adapter and visibility
        socketsFragment_socketsRecyclerView.adapter = socketsAdapter
        socketsFragment_socketsRecyclerView.visibility = View.VISIBLE
    }


}