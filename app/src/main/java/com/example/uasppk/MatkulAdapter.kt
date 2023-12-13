package com.example.uasppk

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.view.menu.MenuView.ItemView
import androidx.recyclerview.widget.RecyclerView
import com.example.uasppk.model.Matkul

class MatkulAdapter(
    private val dataList: List<Matkul> = emptyList(),
    private val onItemClicked: (Matkul) -> Unit
) :

    RecyclerView.Adapter<MatkulAdapter.MatkulViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MatkulViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.row_matkul, parent, false)
        return MatkulViewHolder(view)
    }

    override fun onBindViewHolder(holder: MatkulViewHolder, position: Int) {
        holder.txtNama.text = dataList[position].nama
        holder.txtDesk.text = dataList[position].deskripsi
        holder.txtKategori.text = dataList[position].kategori
        val jumlahSKS = dataList[position].JumlahSKS.toString()
        holder.txtSKS.text = jumlahSKS
        holder.txtSemester.text = dataList[position].periode
        holder.itemView.setOnClickListener { onItemClicked(dataList[position]) }
    }

    override fun getItemCount() = dataList.size

    class MatkulViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val txtNama: TextView
        val txtDesk: TextView
        val txtKategori: TextView
        val txtSKS: TextView
        val txtSemester: TextView

        init {
            txtNama = itemView.findViewById(R.id.txt_nama_matkul)
            txtSemester = itemView.findViewById(R.id.txt_semester)
            txtDesk = itemView.findViewById(R.id.txt_deskripsi_matkul)
            txtSKS = itemView.findViewById(R.id.txt_jumlah_sks)
            txtKategori = itemView.findViewById(R.id.txt_kategori_matkul)
        }
    }
}

