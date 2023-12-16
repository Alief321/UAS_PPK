package com.example.uasppk

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.example.uasppk.model.request.NilaiUpdateRequest
import com.example.uasppk.model.response.NilaiIDResponse
import com.example.uasppk.service.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private const val TOKEN = "token"
private const val ID = "id"
private const val NAMA = "nama"
private const val MATKUL = "matkul"
private const val IDMHS = "idMahasiswa"

class ChangeNilaiFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: NilaiAdapter
    private var nilaiList: List<Nilai> = emptyList()
    private var matkulList: List<Matkul> = emptyList()
    private var listOfMatkul: ArrayList<String> = ArrayList()
    private var token: String? = null
    private var id: Int? = null
    private var idMahasiswa: Int? = null
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
            matkul = it.getString(MATKUL)
            idMahasiswa = it.getInt(IDMHS)
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

        listOfMatkul.add(matkul!!)

        // Create an ArrayAdapter using the string array and a default spinner layout
        val adapter1 = ArrayAdapter(
            this@ChangeNilaiFragment.requireContext(),
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

        val simpan: Button = view.findViewById(R.id.buttonSimpan)
        val hapus: Button = view.findViewById(R.id.buttonDelete)
        val ubah: Button = view.findViewById(R.id.buttonUbah)

        simpan.visibility = View.GONE

        hapus.setOnClickListener {
            val builder = AlertDialog.Builder(this@ChangeNilaiFragment.activity)
            builder.setMessage("Apakah Anda yakin ingin menghapus nilai ini?")
                .setCancelable(false)
                .setPositiveButton("Yes") { dialog, id ->
                    deleteData()
                }
                .setNegativeButton("No") { dialog, id ->
                    dialog.dismiss()
                }
            val alert = builder.create()
            alert.show()
        }

        ubah.setOnClickListener {
            val nilaiTugas = nilaiTugas.text.toString().toFloat()
            val nilaiPraktikum = nilaiPraktikum.text.toString().toFloat()
            val nilaiUts = nilaiUts.text.toString().toFloat()
            val nilaiUas = nilaiUas.text.toString().toFloat()
            updateData(nilaiTugas, nilaiPraktikum, nilaiUts, nilaiUas)
        }

        getData(nilaiTugas, nilaiPraktikum, nilaiUts, nilaiUas, nilaiPraktikumText)
    }

    private fun updateData(
        nilaiTugas: Float?,
        nilaiPraktikum: Float?,
        nilaiUts: Float?,
        nilaiUas: Float?,
    ) {
        val nilaiRequest = NilaiUpdateRequest(nilaiTugas, nilaiPraktikum, nilaiUts, nilaiUas)
        val call = apiService.updateNilai("Bearer " + token, id!!, nilaiRequest)
        call.enqueue(object : Callback<Nilai> {
            override fun onResponse(
                call: Call<Nilai>,
                response: Response<Nilai>,
            ) {
                if (response.isSuccessful) {
                    val nilaiResponse = response.body()

                    if (nilaiResponse!!.message != null) {
                        Toast.makeText(
                            this@ChangeNilaiFragment.activity,
                            "message: ${nilaiResponse!!.message}",
                            Toast.LENGTH_SHORT,
                        ).show()
                    }
                    Toast.makeText(
                        this@ChangeNilaiFragment.activity,
                        "Nilai berhasil diubah",
                        Toast.LENGTH_SHORT,
                    ).show()
                    val changeNilaiFragment = NilaiMahasiswaFragment.newInstance(token!!, idMahasiswa!!, nama!!)
                    val fragmentManager = activity?.supportFragmentManager
                    val fragmentTransaction = fragmentManager?.beginTransaction()
                    fragmentTransaction?.replace(R.id.frame_layout, changeNilaiFragment)
                    fragmentTransaction?.addToBackStack(null)
                    fragmentTransaction?.commit()
                } else {
                    Toast.makeText(
                        this@ChangeNilaiFragment.activity,
                        "Nilai gagal diubah",
                        Toast.LENGTH_SHORT,
                    ).show()
                }
            }

            override fun onFailure(call: Call<Nilai>, t: Throwable) {
                Toast.makeText(
                    this@ChangeNilaiFragment.activity,
                    "Error: ${t.message}",
                    Toast.LENGTH_SHORT,
                ).show()
            }
        })
    }

    private fun getData(
        nilaiTugas: TextView,
        nilaiPraktikum: TextView,
        nilaiUts: TextView,
        nilaiUas: TextView,
        nilaiPraktikumText: TextView,
    ) {
        Log.d("ID_NILAI", "masuk getData id nilai : $id")
        val call = apiService.getNilai("Bearer " + token, id!!)
        call.enqueue(object : Callback<NilaiIDResponse> {
            override fun onResponse(
                call: Call<NilaiIDResponse>,
                response: Response<NilaiIDResponse>,
            ) {
                if (response.isSuccessful) {
                    val nilaiResponse = response.body()
                    nilaiTugas.text = nilaiResponse!!.nilai_Tugas.toString()
                    nilaiPraktikum.text = nilaiResponse!!.nilai_Praktikum.toString()
                    nilaiUts.text = nilaiResponse!!.nilai_UTS.toString()
                    nilaiUas.text = nilaiResponse!!.nilai_UAS.toString()

                    val matkul =
                        apiService.getAllmatkul("Bearer " + token, matkul, null)
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
                                    this@ChangeNilaiFragment.activity,
                                    "Get matkul failed",
                                    Toast.LENGTH_SHORT,
                                ).show()
                            }
                        }

                        override fun onFailure(call: Call<List<Matkul>>, t: Throwable) {
                            Toast.makeText(
                                this@ChangeNilaiFragment.activity,
                                "Error: ${t.message}",
                                Toast.LENGTH_SHORT,
                            ).show()
                        }
                    })
                }
            }

            override fun onFailure(call: Call<NilaiIDResponse>, t: Throwable) {
                Toast.makeText(
                    this@ChangeNilaiFragment.activity,
                    "Error: ${t.message}",
                    Toast.LENGTH_SHORT,
                ).show()
            }
        })
    }

    private fun deleteData() {
        val call = apiService.deleteNilai("Bearer " + token, id!!)
        call.enqueue(object : Callback<Nilai> {
            override fun onResponse(
                call: Call<Nilai>,
                response: Response<Nilai>,
            ) {
                if (response.isSuccessful) {
                    val nilaiResponse = response.body()
                    Toast.makeText(this@ChangeNilaiFragment.activity, "Nilai berhasil dihapus", Toast.LENGTH_SHORT).show()
                    val changeNilaiFragment = NilaiMahasiswaFragment.newInstance(token!!, idMahasiswa!!, nama!!)
                    val fragmentManager = activity?.supportFragmentManager
                    val fragmentTransaction = fragmentManager?.beginTransaction()
                    fragmentTransaction?.replace(R.id.frame_layout, changeNilaiFragment)
                    fragmentTransaction?.addToBackStack(null)
                    fragmentTransaction?.commit()
                }
            }

            override fun onFailure(call: Call<Nilai>, t: Throwable) {
                Toast.makeText(
                    this@ChangeNilaiFragment.activity,
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
        private const val MATKUL = "matkul"

        @JvmStatic
        fun newInstance(token: String, id: Int, nama: String, matkul: String, idMahasiswa: Int) =
            ChangeNilaiFragment().apply {
                arguments = Bundle().apply {
                    putString(TOKEN, token)
                    putInt(ID, id)
                    putString(NAMA, nama)
                    putString(MATKUL, matkul)
                    putInt(IDMHS, idMahasiswa)
                }
            }
    }
}
