package com.mcdev.androidcodingtest.main

import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mcdev.androidcodingtest.adapters.CurrencyAdapter
import com.mcdev.androidcodingtest.model.Data
import com.mcdev.androidcodingtest.model.Rates
import com.mcdev.androidcodingtest.utils.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlin.math.round

class MainViewModel @ViewModelInject constructor(private val repository: MainRepository, private val dispatchers: DispatcherProvider): ViewModel(){

    sealed class CurrencyEvent {
        class Success(val data: Data): CurrencyEvent()
        class Failure(val errorText: String): CurrencyEvent()
        object Loading: CurrencyEvent()
        object Empty: CurrencyEvent()
    }

    /*using state flow*/
    private val mutableStateFlow = MutableStateFlow<CurrencyEvent>(CurrencyEvent.Empty)
    val conversion: StateFlow<CurrencyEvent> = mutableStateFlow
    val _currencies = mutableListOf<Data>()

    fun fetchCurrs() {
        CurrencyAdapter().differ.currentList
    }

    fun convert(apiKey: String, amount: Double, symbol: String, convert: String) {
        val fromAmount = amount.toDouble()
        if (fromAmount == null) {
            mutableStateFlow.value = CurrencyEvent.Failure("Invalid amount")
            Log.d("TAG", "convert: Invalid amount")
            return
        }

        viewModelScope.launch(dispatchers.io) {
            mutableStateFlow.value = CurrencyEvent.Loading
            when(val ratesResponse = repository.getRates(apiKey, amount, symbol, convert)){
                is Resource.Error -> {
                    mutableStateFlow.value = CurrencyEvent.Failure(ratesResponse.message!!)
                    Log.d("TAG", "convert: conversion failed.: " + ratesResponse.data)
                }
                is Resource.Success -> {
                    Log.d("TAG", "convert: conversion successful." )
                    val rates = ratesResponse.data!!.data
//                    val rate = getRateForCurrency(convert, rates)
//                    if (rates == null) {
//                        mutableStateFlow.value = CurrencyEvent.Failure("Unexpected error")
//                    } else {
//                        val convertedCurrency = round(fromAmount * rates.amount * 100) / 100
                        mutableStateFlow.value = CurrencyEvent.Success(rates)
//                    }
                }
            }
        }
    }

        private fun getRateForCurrency(currency: String, rates: Rates) = when (currency) {
            "CAD" -> rates.cAD
            else -> null

    }

}