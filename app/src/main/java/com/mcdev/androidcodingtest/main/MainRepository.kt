package com.mcdev.androidcodingtest.main

import com.mcdev.androidcodingtest.model.CurrencyResponse
import com.mcdev.androidcodingtest.utils.Resource

interface MainRepository {
    //function to access our api
    suspend fun getRates(apiKey: String, amount: Double, symbol: String, convert: String): Resource<CurrencyResponse>
}