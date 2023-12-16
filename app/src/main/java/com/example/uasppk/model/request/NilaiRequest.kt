package com.example.uasppk.model.request

data class NilaiRequest(
    val nilaiTugas: Float,
    val nilaiPraktikum: Float,
    val nilaiUTS: Float,
    val nilaiUAS: Float,
    val mataKuliah: String,
    val idMahasiswa: Long,
)
