package com.mcdev.androidcodingtest.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.view.menu.ActionMenuItemView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.mcdev.androidcodingtest.R
import com.mcdev.androidcodingtest.databinding.ItemCurrencyLayoutBinding
import com.mcdev.androidcodingtest.model.Data
import java.text.DecimalFormat

class CurrencyAdapter: RecyclerView.Adapter<CurrencyAdapter.CurrencyViewHolder>(){

    inner class CurrencyViewHolder(val binding: ItemCurrencyLayoutBinding) : RecyclerView.ViewHolder(binding.root) {

    }

    private val differCallback = object : DiffUtil.ItemCallback<Data>() {
        override fun areItemsTheSame(oldItem: Data, newItem: Data): Boolean {
            return oldItem.quote.BTC.price == newItem.quote.BTC.price//todo
        }

        override fun areContentsTheSame(oldItem: Data, newItem: Data): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrencyViewHolder {
        val binding = ItemCurrencyLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return CurrencyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CurrencyViewHolder, position: Int) {
        val currency = differ.currentList[position]
        Log.d("TAG", "onBindViewHolder: holder price " + currency.quote.BTC.price)

        val decimalFormat = DecimalFormat("00.00000")

        holder.binding.apply {
            this.itemName.text = "Bitcoin"
            this.itemDate.text = currency.quote.BTC.last_updated
            this.itemPrice.text = decimalFormat.format(currency.quote.BTC.price)
            this.itemSymbol.text = "BTC"
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    fun submitList(list: List<Data>) {
        differ.submitList(list)
    }

    fun currentList(): List<Data> {
        return differ.currentList
    }
}