package com.example.uasppk.model

data class Nilai(
    val id: Long,
    val mataKuliah: String,
    val nilaiTugas: Double,
    val nilaiPraktikum: Double,
    val nilaiUTS: Double,
    val nilaiUAS: Double,
    val nilaiHuruf: String,
    val nilaiAngka: Double,
    val bobot: Double,
    val mahasiswa: String,
    val periode: String,
    val message: String,
)
