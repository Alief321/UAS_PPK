package com.example.uasppk

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.SearchView
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.uasppk.client.ApiClient
import com.example.uasppk.model.Matkul
import com.example.uasppk.service.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private const val TOKEN = "token"
private const val ID = "id"

class MatkulFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: MatkulAdapter
    private var matkulList: List<Matkul> = emptyList()
    private var token: String? = null
    private var id: Int? = null
    private var name: String? = null
    private var periode: Long? = null

    private lateinit var apiService: ApiService

    private var contextVariable: Context? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        contextVariable = context
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        apiService = ApiClient.apiService
        Log.d("Cek", "masuk onCreate")
        arguments?.let {
            token = it.getString(TOKEN)
            id = it.getInt(ID)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        Log.d("Cek", "masuk matkul")
        return inflater.inflate(R.layout.matkul_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("Token", token!!)
        Log.d("Cek", "masuk matkul")

        // Get the reference to the spinner
        val spinner: Spinner = view.findViewById(R.id.spinner)

        val listOfSemesters = listOf(
            mapOf("periode" to 99, "item" to "semua"),
            mapOf("periode" to 1, "item" to "semester 1"),
            mapOf("periode" to 2, "item" to "semester 2"),
            mapOf("periode" to 3, "item" to "semester 3"),
            mapOf("periode" to 4, "item" to "semester 4"),
            mapOf("periode" to 5, "item" to "semester 5"),
            mapOf("periode" to 6, "item" to "semester 6"),
            mapOf("periode" to 7, "item" to "semester 7"),
            mapOf("periode" to 8, "item" to "semester 8"),
        )

        // Create an ArrayAdapter using the string array and a default spinner layout
        val adapter1 = ArrayAdapter(
            this@MatkulFragment.requireContext(),
            android.R.layout.simple_spinner_item,
            listOfSemesters.map { it["item"] },
        )

        // Specify the layout to use when the list of choices appears
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        // Apply the adapter to the spinner
        spinner.adapter = adapter1

        // Set a listener to handle item selections
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parentView: AdapterView<*>?,
                selectedItemView: View?,
                position: Int,
                id: Long,
            ) {
                if (listOfSemesters[position]["periode"] == 99) {
                    periode = null
                } else {
                    periode = listOfSemesters[position]["periode"].toString().toLong()
                }
                addData(name, periode)
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {
                // Do nothing here
            }
        }
        val searchView: SearchView = view.findViewById(R.id.searchView)

        // Set a listener to handle query text changes
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                name = query
                addData(name, periode)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                name = newText
                addData(name, periode)
                return true
            }
        })

        recyclerView = view.findViewById(R.id.recyclerView)
        adapter = MatkulAdapter(matkulList) { matkul ->
            Toast.makeText(
                this@MatkulFragment.activity,
                "Anda telah mengklik ${matkul.nama}",
                Toast.LENGTH_SHORT,
            ).show()
        }

        val layoutManager = LinearLayoutManager(this.context)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter

        addData(name, periode)
    }

    private fun addData(name: String?, periode: Long?) {
        val call = apiService.getAllmatkul("Bearer " + token, name, periode)
        call.enqueue(object : Callback<List<Matkul>> {
            override fun onResponse(
                call: Call<List<Matkul>>,
                response: Response<List<Matkul>>,
            ) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    matkulList = responseBody!!
                    adapter.setData(matkulList) // Add this line to update the adapter data
                    adapter.notifyDataSetChanged()
                } else {
                    Toast.makeText(
                        this@MatkulFragment.activity,
                        "Get matkul failed",
                        Toast.LENGTH_SHORT,
                    ).show()
                }
            }

            override fun onFailure(call: Call<List<Matkul>>, t: Throwable) {
                Toast.makeText(
                    this@MatkulFragment.activity,
                    "Error: ${t.message}",
                    Toast.LENGTH_SHORT,
                ).show()
            }
        })
    }

    companion object {
        private const val TOKEN = "token"
        private const val ID = "id"

        @JvmStatic
        fun newInstance(token: String, id: Int) = MatkulFragment().apply {
            arguments = Bundle().apply {
                putString(TOKEN, token)
                putInt(ID, id)
            }
        }
    }
}
