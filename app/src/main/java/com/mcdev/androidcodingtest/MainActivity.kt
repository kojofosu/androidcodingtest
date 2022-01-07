package com.mcdev.androidcodingtest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mcdev.androidcodingtest.adapters.CurrencyAdapter
import com.mcdev.androidcodingtest.databinding.ActivityMainBinding
import com.mcdev.androidcodingtest.enums.BaseCurrency
import com.mcdev.androidcodingtest.main.MainViewModel
import com.mcdev.androidcodingtest.model.Rates
import com.mcdev.androidcodingtest.utils.API_KEY
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import java.text.DecimalFormat

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val convert = "BTC"
    private val base_currencies: ArrayList<BaseCurrency> = arrayListOf<BaseCurrency>(BaseCurrency.UGX, BaseCurrency.USD, BaseCurrency.EUR)
    private val viewModel: MainViewModel by viewModels()
    private val currencyAdapter: CurrencyAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(LayoutInflater.from(this))
        val root = binding.root
        setContentView(root)

        val currencyAdapter = CurrencyAdapter()
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            hasFixedSize()
            adapter = currencyAdapter
        }
        binding.currencySpinner.adapter = ArrayAdapter<BaseCurrency>(this, android.R.layout.simple_list_item_1, base_currencies)

        var amount = binding.amountEt.text.trim().toString().toDoubleOrNull()
        var symbol: String = binding.currencySpinner.selectedItem.toString()

        binding.amountEt.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (p0.isNullOrBlank()) {
                    Toast.makeText(this@MainActivity, "Amount should be greater than 0.0", Toast.LENGTH_SHORT).show()
                } else {
                    amount = p0.trim().toString().toDouble()
                }

            }

            override fun afterTextChanged(p0: Editable?) {
                //do conversion
                if (amount == null) {
                    Toast.makeText(this@MainActivity, "Amount should be greater than 0.0", Toast.LENGTH_SHORT).show()
                } else {
                    viewModel.convert(API_KEY, amount!!, symbol, convert)
                }

            }

        })

        binding.currencySpinner.onItemSelectedListener = object:  AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                symbol = binding.currencySpinner.selectedItem.toString()
                if (amount == null) {
                    Toast.makeText(this@MainActivity, "Amount should be greater than 0.0", Toast.LENGTH_SHORT).show()
                } else {
                    viewModel.convert(API_KEY, amount!!, symbol, convert)
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                //TODO("Not yet implemented")
            }

        }

        //collect events from state flow
        lifecycleScope.launchWhenStarted {
            viewModel.conversion.collect{
                when (it) {
                    is MainViewModel.CurrencyEvent.Success -> {
                        //what to do when success
                        binding.progressBar.visibility = View.GONE
                        Log.d("TAG", "onCreate: Success ${it.data}")
                        Toast.makeText(this@MainActivity, "Success", Toast.LENGTH_SHORT).show()
                        currencyAdapter.submitList(listOf(it.data))

                        Log.d("TAG", "onCreate: list of data: " + currencyAdapter.currentList())
                    }
                    is MainViewModel.CurrencyEvent.Failure -> {
                        //do when failure
                        binding.progressBar.visibility = View.GONE
                        Toast.makeText(this@MainActivity, "Failure", Toast.LENGTH_SHORT).show()
                        Log.d("TAG", "onCreate: Failure ${it.errorText}")

                    }
                    is MainViewModel.CurrencyEvent.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE

                    }
                    else -> Unit
                }
            }
        }
    }
}