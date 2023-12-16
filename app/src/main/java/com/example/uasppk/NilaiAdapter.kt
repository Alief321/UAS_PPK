package com.example.uasppk

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.uasppk.model.Nilai

class NilaiAdapter(
    private var dataList: List<Nilai> = emptyList(),
    private val onItemClicked: (Nilai) -> Unit,
) :

    RecyclerView.Adapter<NilaiAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.row_nilai, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.txtMatkul.text = dataList[position].mataKuliah
        holder.txtTugas.text = "Nilai Tugas : " + dataList[position].nilaiTugas.toString()
        holder.txtPraktikum.text =
            "Nilai Praktikum : " + dataList[position].nilaiPraktikum.toString()
        holder.txtUTS.text = "Nilai UTS : " + dataList[position].nilaiUTS.toString()
        holder.txtUAS.text = "Nilai UAS : " + dataList[position].nilaiUAS.toString()
        holder.txtAngka.text = "Nilai Angka : " + dataList[position].nilaiAngka.toString()
        holder.txtHuruf.text = "Nilai Huruf : " + dataList[position].nilaiHuruf
        holder.txtBobot.text = "Bobot : " + dataList[position].bobot.toString()
        holder.txtSemester.text = "Semester : " + dataList[position].periode.substring(9)
        holder.itemView.setOnClickListener { onItemClicked(dataList[position]) }
    }

    override fun getItemCount() = dataList.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtMatkul: TextView
        val txtTugas: TextView
        val txtPraktikum: TextView
        val txtUTS: TextView
        val txtUAS: TextView
        val txtAngka: TextView
        val txtHuruf: TextView
        val txtBobot: TextView
        val txtSemester: TextView

        init {
            txtMatkul = itemView.findViewById(R.id.textMatkul)
            txtTugas = itemView.findViewById(R.id.textTugas)
            txtPraktikum = itemView.findViewById(R.id.textPraktikum)
            txtUTS = itemView.findViewById(R.id.textUTS)
            txtUAS = itemView.findViewById(R.id.textUAS)
            txtAngka = itemView.findViewById(R.id.textAngka)
            txtHuruf = itemView.findViewById(R.id.textHuruf)
            txtBobot = itemView.findViewById(R.id.textBobot)
            txtSemester = itemView.findViewById(R.id.textSemesterNilai)
        }
    }

    fun setData(newData: List<Nilai>) {
        dataList = newData
    }
}
