package com.example.uasppk

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.uasppk.model.Matkul
import com.example.uasppk.model.response.ProfileResponse
import com.example.uasppk.service.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


private const val TOKEN = "token"
private const val ID = "id"
class MatkulFragment: Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: MatkulAdapter
    private var matkulList: List<Matkul> = emptyList()
    private var token: String? = null
    private var id: Int? = null

    private lateinit var apiService: ApiService

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
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d("Cek", "masuk matkul")
        return inflater.inflate(R.layout.matkul_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("Token", token!!)
        Log.d("Cek", "masuk matkul")
        addData()

        recyclerView = view.findViewById(R.id.recycler_view)
        adapter = MatkulAdapter(matkulList) { matkul ->
            Toast.makeText(this@MatkulFragment.activity, "Anda telah mengklik ${matkul.nama}", Toast.LENGTH_LONG).show()
        }

        val layoutManager = LinearLayoutManager(this@MatkulFragment.activity)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter
    }

    private fun addData() {
        val call = apiService.getAllmatkul("Bearer "+ token)
        call.enqueue(object : Callback<List<Matkul>> {
            override fun onResponse(
                call: Call<List<Matkul>>,
                response: Response<List<Matkul>>
            ) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    matkulList = responseBody!!
                } else {
                    Toast.makeText(this@MatkulFragment.activity, "Get matkul failed", Toast.LENGTH_SHORT)
                        .show()
                }
            }
            override fun onFailure(call: Call<List<Matkul>>, t: Throwable) {
                Toast.makeText(this@MatkulFragment.activity, "Error: ${t.message}", Toast.LENGTH_SHORT)
                    .show()
            }
        })
    }

    companion object {
        private const val TOKEN = "token"
        private const val ID = "id"

        @JvmStatic
        fun newInstance(token: String, id: Int) =
            MatkulFragment().apply {
                arguments = Bundle().apply {
                    putString(TOKEN, token)
                    putInt(ID, id)
                }
            }
    }

}