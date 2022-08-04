package com.raffaellmir.exchangerate.presentation.currency

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.raffaellmir.exchangerate.databinding.CurrencyItemBinding
import com.raffaellmir.exchangerate.domain.model.Currency

class CurrencyAdapter(private val onFavoriteButtonClick: (Currency) -> Unit) :
    ListAdapter<Currency, CurrencyAdapter.CurrencyHolder>(ItemDiffCallback) {

    inner class CurrencyHolder(
        private val binding: CurrencyItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(currency: Currency) = with(binding) {
            tvCurrencyTitle.text = currency.symbol
            tvCurrencyValue.text = currency.value.toString()

            cbIsFavorite.isChecked = currency.favorite
            cbIsFavorite.setOnClickListener { onFavoriteButtonClick(currency) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrencyHolder =
        CurrencyHolder(CurrencyItemBinding
            .inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: CurrencyHolder, position: Int) =
        holder.bind(getItem(position))

    private companion object {
        object ItemDiffCallback : DiffUtil.ItemCallback<Currency>() {
            override fun areItemsTheSame(oldItem: Currency, newItem: Currency): Boolean = oldItem.symbol == newItem.symbol
            override fun areContentsTheSame(oldItem: Currency, newItem: Currency): Boolean = oldItem == newItem
        }
    }
}