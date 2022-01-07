package com.mcdev.androidcodingtest.model

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface CurrencyApi {

    @GET("/v1/tools/price-conversion")
    suspend fun getRates(
        @Query(value = "CMC_PRO_API_KEY") api_key: String,
        @Query(value = "amount") amount: Double,
        @Query(value = "symbol") symbol: String,
        @Query(value = "convert") convert: String
    ): Response<CurrencyResponse>
//    suspend fun getRates(@Query(value = "base") baseCurrency: String): Response<CurrencyResponse>
}