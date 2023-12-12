package com.example.uasppk

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment

private const val NAME = "name"
private const val TOKEN = "token"

class HomeFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var name: String? = null
    private var token: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            name = it.getString(NAME)
            token = it.getString(TOKEN)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d("HomeFragment", "onCreateView called")
        return inflater.inflate(R.layout.home_fragment, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("HomeFragment", "onVIEWCREATED called")
        // Find the TextView by its ID
        val myTextView = view.findViewById<TextView>(R.id.haloUser)
        // Now, you can use `myTextView` to set text or perform other operations
        myTextView.text = name
    }

    companion object {
        private const val NAME = "param1"
        private const val TOKEN = "param2"

        @JvmStatic
        fun newInstance(name: String, token: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putString(NAME, name)
                    putString(TOKEN, token)
                }
            }
    }
}