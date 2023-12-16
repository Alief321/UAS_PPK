package com.example.uasppk

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.uasppk.client.ApiClient
import com.example.uasppk.model.Nilai
import com.example.uasppk.service.ApiService
import com.google.android.material.floatingactionbutton.FloatingActionButton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private const val TOKEN = "token"
private const val ID = "id"
private const val NAMA = "nama"

class NilaiMahasiswaFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: NilaiAdapter
    private var nilaiList: List<Nilai> = emptyList()
    private var token: String? = null
    private var id: Int? = null
    private var nama: String? = null
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
            nama = it.getString(NAMA)
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
        return inflater.inflate(R.layout.nilai_mahasiswa_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("Token", token!!)
        Log.d("Cek", "masuk matkul")

        // Get the reference to the spinner
        val spinner: Spinner = view.findViewById(R.id.spinner3)

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
            this@NilaiMahasiswaFragment.requireContext(),
            android.R.layout.simple_spinner_item,
            listOfSemesters.map { it["item"] },
        )

        val namaMHS = view.findViewById<TextView>(R.id.NamaMahasiswa)
        namaMHS.text = "Nama Mahasiswa: ${arguments?.getString("nama")}"
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
                addData(periode)
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {
                // Do nothing here
            }
        }

        val tambahButton: FloatingActionButton = view.findViewById(R.id.tambahButton)
        tambahButton.setOnClickListener {
            val createNilaiFragment =
                CreateNilaiFragment.newInstance(token!!, id!!, nama!!)
            val fragmentManager = activity?.supportFragmentManager
            val fragmentTransaction = fragmentManager?.beginTransaction()
            fragmentTransaction?.replace(R.id.frame_layout, createNilaiFragment)
            fragmentTransaction?.addToBackStack(null)
            fragmentTransaction?.commit()
        }

        recyclerView = view.findViewById(R.id.recyclerView)
        adapter = NilaiAdapter(nilaiList) { nilai ->
            Log.d("ID_NILAI", "item clicked dengan id Nilai : ${nilai.id}")
            val changeNilaiFragment =
                ChangeNilaiFragment.newInstance(token!!, nilai.id.toInt(), nama!!, nilai.mataKuliah, id!!)
            val fragmentManager = activity?.supportFragmentManager
            val fragmentTransaction = fragmentManager?.beginTransaction()
            fragmentTransaction?.replace(R.id.frame_layout, changeNilaiFragment)
            fragmentTransaction?.addToBackStack(null)
            fragmentTransaction?.commit()
        }

        val layoutManager = LinearLayoutManager(this.context)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter

        addData(periode)
    }

    private fun addData(periode: Long?) {
        val call = apiService.getNilaiMahasiswa("Bearer " + token, id!!, periode)
        call.enqueue(object : Callback<List<Nilai>> {
            override fun onResponse(
                call: Call<List<Nilai>>,
                response: Response<List<Nilai>>,
            ) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    nilaiList = responseBody!!
                    adapter.setData(nilaiList) // Add this line to update the adapter data
                    adapter.notifyDataSetChanged()
                } else {
                    Toast.makeText(
                        this@NilaiMahasiswaFragment.activity,
                        "Get nilai failed",
                        Toast.LENGTH_SHORT,
                    ).show()
                }
            }

            override fun onFailure(call: Call<List<Nilai>>, t: Throwable) {
                Toast.makeText(
                    this@NilaiMahasiswaFragment.activity,
                    "Error: ${t.message}",
                    Toast.LENGTH_SHORT,
                ).show()
            }
        })
    }

    companion object {
        private const val TOKEN = "token"
        private const val ID = "id"
        private const val NAMA = "nama"

        @JvmStatic
        fun newInstance(token: String, id: Int, nama: String) = NilaiMahasiswaFragment().apply {
            arguments = Bundle().apply {
                putString(TOKEN, token)
                putInt(ID, id)
                putString(NAMA, nama)
            }
        }
    }
}
