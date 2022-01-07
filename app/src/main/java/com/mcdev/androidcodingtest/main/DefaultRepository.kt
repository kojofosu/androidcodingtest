package com.mcdev.androidcodingtest.main

import android.util.Log
import com.mcdev.androidcodingtest.model.CurrencyApi
import com.mcdev.androidcodingtest.model.CurrencyResponse
import com.mcdev.androidcodingtest.utils.Resource
import javax.inject.Inject

class DefaultRepository @Inject constructor(private val api: CurrencyApi): MainRepository {
    override suspend fun getRates(apiKey: String, amount: Double, symbol: String, convert: String): Resource<CurrencyResponse> {
        return try {
            val response = api.getRates(apiKey, amount, symbol, convert)
            val result = response.body()
            if (response.isSuccessful && result != null) {
                Resource.Success(result)
            } else {
                Resource.Error(response.message())

            }

        } catch (e: Exception) {
            Resource.Error(e.message ?: "An error occurred")
        }
    }

}