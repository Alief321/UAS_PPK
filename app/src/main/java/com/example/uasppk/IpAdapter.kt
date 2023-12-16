package com.example.uasppk

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.uasppk.model.IPS

class IpAdapter(
    private var dataList: List<IPS> = emptyList(),
    private val onItemClicked: (IPS) -> Unit,
) :

    RecyclerView.Adapter<IpAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.ipsemester, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.txtIP.text = "IP : " + dataList[position].ips.toString()
        holder.txtSemester.text = "Semester : " + dataList[position].periode.substring(9)
        holder.itemView.setOnClickListener { onItemClicked(dataList[position]) }
    }

    override fun getItemCount() = dataList.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtIP: TextView
        val txtSemester: TextView

        init {
            txtIP = itemView.findViewById(R.id.textIP)
            txtSemester = itemView.findViewById(R.id.textSemesterIP)
        }
    }

    fun setData(newData: List<IPS>) {
        dataList = newData
    }
}
