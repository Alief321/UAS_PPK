package com.example.uasppk

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.uasppk.model.User

class MahasiswaAdapter(
    private var dataList: List<User> = emptyList(),
    private val onItemClicked: (User) -> Unit,
) :

    RecyclerView.Adapter<MahasiswaAdapter.MahasiswaViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MahasiswaViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.row_mahasiswa, parent, false)
        return MahasiswaViewHolder(view)
    }

    override fun onBindViewHolder(holder: MahasiswaViewHolder, position: Int) {
        holder.txtNama.text = dataList[position].name
        holder.txtKelas.text = "Kelas : " + dataList[position].kelas
        holder.txtNim.text = "NIM : " + dataList[position].nim
        holder.txtId = dataList[position].id.toInt()
        holder.itemView.setOnClickListener { onItemClicked(dataList[position]) }
    }

    override fun getItemCount() = dataList.size

    class MahasiswaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var txtId: Int
        val txtNama: TextView
        val txtKelas: TextView
        val txtNim: TextView

        init {
            txtId = 0
            txtNama = itemView.findViewById(R.id.nama)
            txtKelas = itemView.findViewById(R.id.kelas)
            txtNim = itemView.findViewById(R.id.nim)
        }
    }

    fun setData(newData: List<User>) {
        dataList = newData
    }
}
