package com.example.uasppk

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.uasppk.model.Matkul

class MatkulAdapter(
    private var dataList: List<Matkul> = emptyList(),
    private val onItemClicked: (Matkul) -> Unit,
) :

    RecyclerView.Adapter<MatkulAdapter.MatkulViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MatkulViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.row_matkul, parent, false)
        return MatkulViewHolder(view)
    }

    override fun onBindViewHolder(holder: MatkulViewHolder, position: Int) {
        holder.txtNama.text = dataList[position].nama
        holder.txtDesk.text = "Deskripsi : " + dataList[position].deskripsi
        holder.txtKategori.text = "Kategori : " + dataList[position].kategori
        val jumlahSKS = dataList[position].jumlahSKS.toString()
        holder.txtSKS.text = "Jumlah SKS : " + jumlahSKS
        holder.txtSemester.text = "Semester : " + dataList[position].periode.substring(9)
        holder.itemView.setOnClickListener { onItemClicked(dataList[position]) }
    }

    override fun getItemCount() = dataList.size

    class MatkulViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
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
    fun setData(newData: List<Matkul>) {
        dataList = newData
    }
}
