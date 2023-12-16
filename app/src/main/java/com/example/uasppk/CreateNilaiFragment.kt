package com.example.uasppk

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.uasppk.client.ApiClient
import com.example.uasppk.model.Matkul
import com.example.uasppk.model.Nilai
import com.example.uasppk.model.request.NilaiRequest
import com.example.uasppk.model.response.ProfileResponse
import com.example.uasppk.service.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private const val TOKEN = "token"
private const val ID = "id"
private const val NAMA = "nama"

class CreateNilaiFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: NilaiAdapter
    private var nilaiList: List<Nilai> = emptyList()
    private var matkulList: List<Matkul> = emptyList()
    private var listOfMatkul: ArrayList<String> = ArrayList()
    private var token: String? = null
    private var id: Int? = null
    private var nama: String? = null
    private var matkul: String? = null

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
        return inflater.inflate(R.layout.create_nilai, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("Token", token!!)
        Log.d("Cek", "masuk matkul")

        // Get the reference to the spinner
        val spinner: Spinner = view.findViewById(R.id.spinnerCreateNilai)

        listOfMatkul.add("Pilih Matakuliah")
        getMatkulAmpu()

        // Create an ArrayAdapter using the string array and a default spinner layout
        val adapter1 = ArrayAdapter(
            this@CreateNilaiFragment.requireContext(),
            android.R.layout.simple_spinner_item,
            listOfMatkul,
        )

        val namaMHS = view.findViewById<TextView>(R.id.namaMahasiswa)
        namaMHS.text = nama
        // Specify the layout to use when the list of choices appears
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        // Apply the adapter to the spinner
        spinner.adapter = adapter1

        val nilaiTugas: TextView = view.findViewById(R.id.tugasText)
        val nilaiPraktikum: TextView = view.findViewById(R.id.praktikumText)
        val nilaiUts: TextView = view.findViewById(R.id.utsText)
        val nilaiUas: TextView = view.findViewById(R.id.uasText)
        val nilaiPraktikumText: TextView = view.findViewById(R.id.nilaiPraktikumText)

        // Set a listener to handle item selections
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parentView: AdapterView<*>?,
                selectedItemView: View?,
                position: Int,
                id: Long,
            ) {
                matkul = listOfMatkul[position]!!.toString()
                if (matkul != "Pilih Matakuliah") {
                    getMatkul(matkul, nilaiPraktikum, nilaiPraktikumText)
                }
                Log.d("matkulselected", matkul!!)
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {
                // Do nothing here
            }
        }

        val simpan: Button = view.findViewById(R.id.buttonSimpan)
        val hapus: Button = view.findViewById(R.id.buttonDelete)
        val ubah: Button = view.findViewById(R.id.buttonUbah)

        hapus.visibility = View.GONE
        ubah.visibility = View.GONE

        simpan.setOnClickListener() {
            Log.d(
                "cek Prak",
                nilaiPraktikum.text.toString() + " " + nilaiPraktikum.text.toString().isEmpty(),
            )
            if (nilaiTugas.text.toString().isNotEmpty() && nilaiPraktikum.text.toString()
                    .isNotEmpty() && nilaiUts.text.toString()
                    .isNotEmpty() && nilaiUas.text.toString().isNotEmpty()
            ) {
                addData(
                    nilaiTugas.text.toString().toFloat(),
                    nilaiPraktikum.text.toString().toFloat(),
                    nilaiUts.text.toString().toFloat(),
                    nilaiUas.text.toString().toFloat(),
                    matkul.toString(),
                    id!!.toLong(),
                )
            } else if (nilaiPraktikum.text.toString().isEmpty()) {
                addData(
                    nilaiTugas.text.toString().toFloat(),
                    0.0F,
                    nilaiUts.text.toString().toFloat(),
                    nilaiUas.text.toString().toFloat(),
                    matkul.toString(),
                    id!!.toLong(),
                )
            } else {
                Toast.makeText(
                    this@CreateNilaiFragment.activity,
                    "Nilai tidak boleh kosong",
                    Toast.LENGTH_SHORT,
                ).show()
            }
        }
    }

    private fun getMatkulAmpu() {
        val profile = apiService.profile("Bearer " + token)
        profile.enqueue(object : Callback<ProfileResponse> {
            override fun onResponse(
                call: Call<ProfileResponse>,
                response: Response<ProfileResponse>,
            ) {
                if (response.isSuccessful) {
                    val profileResponse = response.body()
                    val matkul = profileResponse?.matkulAmpu
                    for (i in matkul!!) {
                        Log.d("matkul", i)
                        val call =
                            apiService.getAllmatkul("Bearer " + token, i.toString(), null)
                        call.enqueue(object : Callback<List<Matkul>> {
                            override fun onResponse(
                                call: Call<List<Matkul>>,
                                response: Response<List<Matkul>>,
                            ) {
                                if (response.isSuccessful) {
                                    val responseBody = response.body()
                                    matkulList = responseBody!!
                                    matkulList.forEach() {
                                        listOfMatkul.add(it.nama)
                                        Log.d("matkul", it.nama)
                                    }
                                } else {
                                    Toast.makeText(
                                        this@CreateNilaiFragment.activity,
                                        "Get matkul failed",
                                        Toast.LENGTH_SHORT,
                                    )
                                        .show()
                                }
                            }

                            override fun onFailure(call: Call<List<Matkul>>, t: Throwable) {
                                TODO("Not yet implemented")
                            }
                        })
                    }
                } else {
                    Toast.makeText(
                        this@CreateNilaiFragment.activity,
                        "Get profile failed",
                        Toast.LENGTH_SHORT,
                    )
                        .show()
                }
            }

            override fun onFailure(call: Call<ProfileResponse>, t: Throwable) {
                Toast.makeText(
                    this@CreateNilaiFragment.activity,
                    "Error: ${t.message}",
                    Toast.LENGTH_SHORT,
                ).show()
            }
        })
    }

    private fun addData(
        nilaiTugas: Float,
        nilaiPraktikum: Float,
        nilaiUts: Float,
        nilaiUas: Float,
        matakuliah: String,
        idMahasiswa: Long,
    ) {
        val nilaiRequest: NilaiRequest
        if (nilaiPraktikum != 0.0F) {
            nilaiRequest =
                NilaiRequest(
                    nilaiTugas,
                    nilaiPraktikum,
                    nilaiUts,
                    nilaiUas,
                    matakuliah,
                    idMahasiswa,
                )
        } else {
            nilaiRequest =
                NilaiRequest(
                    nilaiTugas,
                    0.0F,
                    nilaiUts,
                    nilaiUas,
                    matakuliah,
                    idMahasiswa,
                )
        }

        val call = apiService.createNilai("Bearer " + token, nilaiRequest)
        call.enqueue(object : Callback<Nilai> {
            override fun onResponse(
                call: Call<Nilai>,
                response: Response<Nilai>,
            ) {
                if (response.isSuccessful) {
                    val nilaiResponse = response.body()

                    if (nilaiResponse!!.message != null) {
                        Toast.makeText(
                            this@CreateNilaiFragment.activity,
                            "message: ${nilaiResponse!!.message}",
                            Toast.LENGTH_SHORT,
                        ).show()
                        return
                    }
                    Toast.makeText(
                        this@CreateNilaiFragment.activity,
                        "Nilai berhasil ditambahkan",
                        Toast.LENGTH_SHORT,
                    ).show()
                    val createNilaiFragment =
                        NilaiMahasiswaFragment.newInstance(token!!, id!!, nama!!)
                    val fragmentManager = activity?.supportFragmentManager
                    val fragmentTransaction = fragmentManager?.beginTransaction()
                    fragmentTransaction?.replace(R.id.frame_layout, createNilaiFragment)
                    fragmentTransaction?.addToBackStack(null)
                    fragmentTransaction?.commit()
                } else {
                    Toast.makeText(
                        this@CreateNilaiFragment.activity,
                        "Nilai gagal ditambahkan",
                        Toast.LENGTH_SHORT,
                    )
                        .show()
                }
            }

            override fun onFailure(call: Call<Nilai>, t: Throwable) {
                TODO("Not yet implemented")
                Toast.makeText(
                    this@CreateNilaiFragment.activity,
                    "Error: ${t.message}",
                    Toast.LENGTH_SHORT,
                ).show()
            }
        })
    }

    private fun getMatkul(
        nama: String?,
        nilaiPraktikum: TextView,
        nilaiPraktikumText: TextView,
    ) {
        val matkul =
            apiService.getAllmatkul("Bearer " + token, nama, null)
        matkul.enqueue(object : Callback<List<Matkul>> {
            override fun onResponse(
                call: Call<List<Matkul>>,
                response: Response<List<Matkul>>,
            ) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    matkulList = responseBody!!
                    if (matkulList[0].kategori == "T") {
                        nilaiPraktikum.visibility = View.GONE
                        nilaiPraktikumText.visibility = View.GONE
                    } else {
                        nilaiPraktikum.visibility = View.VISIBLE
                        nilaiPraktikumText.visibility = View.VISIBLE
                    }
                } else {
                    Toast.makeText(
                        this@CreateNilaiFragment.activity,
                        "Get matkul failed",
                        Toast.LENGTH_SHORT,
                    ).show()
                }
            }

            override fun onFailure(call: Call<List<Matkul>>, t: Throwable) {
                Toast.makeText(
                    this@CreateNilaiFragment.activity,
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
        fun newInstance(token: String, id: Int, nama: String) =
            CreateNilaiFragment().apply {
                arguments = Bundle().apply {
                    putString(TOKEN, token)
                    putInt(ID, id)
                    putString(NAMA, nama)
                }
            }
    }
}
