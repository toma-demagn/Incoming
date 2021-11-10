package com.example.tutorapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup


import android.R
import android.app.Activity
import android.content.Intent
import kotlinx.android.synthetic.main.fragment_home.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        // Inflate the layout for this fragment
        return inflater.inflate(com.example.tutorapp.R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val names = arrayOf("math","physiquee", "math","physiquee")
        val decs = arrayOf("descmath","descphysic ceci est le description d'un cours de physique vraiment très longue car il faut tester de voir si on peut scroller qzemoifjzqef qzefliijqzeflkzqenf oiqze fmoqzief jqzomeijf zqmoeijf qzemoif nqzoeif jqzeomif  n,qzelmkfnzeqomif jzq zeqflkksdclkqs,dfmo qzef qse:lkfsqdofin ze fzeqofisdlkfemqzlijf dsmiozefqze:jfnzeq lfo nqlkjvnqloi fnqsdoiefjzeqoijf qse lkzd o zeif zeoif qezoifj zeqmlkf ,dslk, zeifj ezqmoif jzqemofi eqzmo ifndvnomqinf z  ze voilà", "descmath","descphysic")
        val pics = arrayOf(com.example.tutorapp.R.drawable.maths, com.example.tutorapp.R.drawable.physique, com.example.tutorapp.R.drawable.maths, com.example.tutorapp.R.drawable.physique)
        val custAdapter = CustomAdapter(context as Activity, names, decs, pics)
        hf_listView.adapter = custAdapter
        hf_listView.setOnItemClickListener { parent, view, position, id ->
            val name = names[position]
            val desc = decs[position]
            val pic = pics[position]
            val intent = Intent(context as Activity, ShowAd::class.java)
            intent.putExtra("name", name)
            intent.putExtra("desc", desc)
            intent.putExtra("pic", pic)
            startActivity(intent)
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HomeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}