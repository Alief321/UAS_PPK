package com.example.uasppk

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.uasppk.client.QuotesAPIClient
import com.example.uasppk.model.Quotes
import com.example.uasppk.service.QuotesAPIService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private const val NAME = "name"
private const val TOKEN = "token"

class HomeFragment : Fragment() {

    private lateinit var apiService: QuotesAPIService
    private var name: String? = null
    private var token: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        apiService = QuotesAPIClient.apiService
        arguments?.let {
            name = it.getString(NAME)
            token = it.getString(TOKEN)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        Log.d("HomeFragment", "onCreateView called")
        return inflater.inflate(R.layout.home_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("HomeFragment", "onVIEWCREATED called")
        // Find the TextView by its ID
        val myTextView = view.findViewById<TextView>(R.id.haloUser)
        var quotesText: TextView = view.findViewById(R.id.quotes)
        // Now, you can use `myTextView` to set text or perform other operations
        myTextView.text = name
        getQuotes(quotesText)
    }

    fun getQuotes(quotes: TextView) {
        val call = apiService.getQuotes()

        call.enqueue(object : Callback<Quotes> {
            override fun onResponse(
                call: Call<Quotes>,
                response: Response<Quotes>,
            ) {
                if (response.isSuccessful) {
                    val profileResponse = response.body()
                    quotes.text = "'" + profileResponse!!.q + "' ~" + profileResponse.a
                } else {
                    Toast.makeText(
                        this@HomeFragment.activity,
                        "Get Quotes failed",
                        Toast.LENGTH_SHORT,
                    )
                        .show()
                }
            }

            override fun onFailure(call: Call<Quotes>, t: Throwable) {
                Toast.makeText(
                    this@HomeFragment.activity,
                    "Error: ${t.message}",
                    Toast.LENGTH_SHORT,
                )
                    .show()
            }
        })
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
