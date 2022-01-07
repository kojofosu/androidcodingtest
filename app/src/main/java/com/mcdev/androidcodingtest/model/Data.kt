package com.mcdev.androidcodingtest.model

data class Data(
    val amount: Double,
    val id: String,
    val last_updated: String,
    val name: String,
    val quote: Quote,
    val symbol: String
)