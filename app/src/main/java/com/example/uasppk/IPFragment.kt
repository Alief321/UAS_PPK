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
import com.db.williamchart.view.BarChartView
import com.db.williamchart.view.LineChartView
import com.example.uasppk.client.ApiClient
import com.example.uasppk.model.IPS
import com.example.uasppk.service.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class IPFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: IpAdapter
    private lateinit var barChartView: BarChartView
    private lateinit var ipk: TextView
    private var ipList: List<IPS> = emptyList()
    private var token: String? = null
    private var id: Int? = null
    private var periode: Long? = null

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
        val view = inflater.inflate(R.layout.ip_fragment, container, false)
        barChartView = view.findViewById(R.id.barChart)
        ipk = view.findViewById(R.id.IPK)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("Token", token!!)
        Log.d("Cek", "masuk matkul")

        recyclerView = view.findViewById(R.id.recyclerView)
        adapter = IpAdapter(ipList) { ip ->
            Toast.makeText(
                this.activity,
                "Anda telah mengklik ${ip.ips}",
                Toast.LENGTH_SHORT,
            ).show()
        }
        addData()

        val layoutManager =
            LinearLayoutManager(this.requireContext(), LinearLayoutManager.HORIZONTAL, false)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter
    }

    private fun addData() {
        val call = apiService.getIPS("Bearer " + token)
        call.enqueue(object : Callback<List<IPS>> {
            override fun onResponse(
                call: Call<List<IPS>>,
                response: Response<List<IPS>>,
            ) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    ipList = responseBody!!
                    val ipTotal: Float = ipList.sumByDouble { it.ips.toDouble() }.toFloat()
                    val ipkdata: Float = ipTotal / ipList.size
                    ipk.text = "IPK : $ipkdata"
                    val lineSet = listOf(
                        "" to 0f,
                    ).toMutableList()
                    for (i in 0 until ipList.size) {
                        lineSet += "Semester ${i + 1}" to ipList[i].ips.toFloat()
                    }
                    lineSet.removeAt(0)
                    barChartView.onDataPointTouchListener = { index, _, _ ->
                        Toast.makeText(
                            this@IPFragment.activity,
                            "${lineSet.toList()[index].first} dengan ip ${lineSet.toList()[index].second}",
                            Toast.LENGTH_SHORT,
                        ).show()
                    }
                    barChartView.animation.duration = 1000L
                    barChartView.animate(lineSet)
                    adapter.setData(ipList) // Add this line to update the adapter data
                    adapter.notifyDataSetChanged()
                } else {
                    Toast.makeText(
                        this@IPFragment.activity,
                        "Get matkul failed",
                        Toast.LENGTH_SHORT,
                    ).show()
                }
            }

            override fun onFailure(call: Call<List<IPS>>, t: Throwable) {
                Toast.makeText(
                    this@IPFragment.activity,
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
        fun newInstance(token: String, id: Int) = IPFragment().apply {
            arguments = Bundle().apply {
                putString(TOKEN, token)
                putInt(ID, id)
            }
        }
    }
}
