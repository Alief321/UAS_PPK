package com.example.uasppk

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.uasppk.client.ApiClient
import com.example.uasppk.model.User
import com.example.uasppk.service.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private const val TOKEN = "token"
private const val ID = "id"

class NilaiDosenFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: MahasiswaAdapter
    private var mahasiswaList: List<User> = emptyList()
    private var token: String? = null
    private var id: Int? = null
    private var name: String? = null
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
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        Log.d("Cek", "masuk matkul")
        return inflater.inflate(R.layout.nilai_dosen_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("Token", token!!)

        val search: SearchView = view.findViewById(R.id.searchView2)

        search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                getMahasiswa(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                getMahasiswa(newText)
                return true
            }
        })

        recyclerView = view.findViewById(R.id.recyclerView3)
        adapter = MahasiswaAdapter(mahasiswaList) { mahasiswa ->
            Log.d("Cek", "item clicked")
            val fragment = NilaiMahasiswaFragment.newInstance(token!!, mahasiswa.id.toInt(), mahasiswa.name)
            val fragmentManager = requireActivity().supportFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.frame_layout, fragment)
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)
        getMahasiswa(name)
    }

    private fun getMahasiswa(name: String?) {
        apiService.getAllMahasiswa("Bearer " + token, name).enqueue(
            object : Callback<List<User>> {
                override fun onResponse(
                    call: Call<List<User>>,
                    response: Response<List<User>>,
                ) {
                    if (response.isSuccessful) {
                        mahasiswaList = response.body()!!
                        adapter.setData(mahasiswaList)
                        adapter.notifyDataSetChanged()
                    }
                }

                override fun onFailure(call: Call<List<User>>, t: Throwable) {
                    Log.d("Cek", "error")
                }
            },
        )
    }

    companion object {
        private const val TOKEN = "token"
        private const val ID = "id"

        @JvmStatic
        fun newInstance(token: String, id: Int) = NilaiDosenFragment().apply {
            arguments = Bundle().apply {
                putString(TOKEN, token)
                putInt(ID, id)
            }
        }
    }
}
