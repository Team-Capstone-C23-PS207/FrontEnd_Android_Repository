package com.example.scumapps.service

import com.google.gson.annotations.SerializedName

data class ScanResponse(

	@field:SerializedName("Rekomendasi")
	val rekomendasi: List<RekomendasiItem>,

	@field:SerializedName("HasilDeteksi")
	val hasilDeteksi: String
)

data class RekomendasiItem(

	@field:SerializedName("link")
	val link: String,

	@field:SerializedName("﻿title")
	val title: String
)
