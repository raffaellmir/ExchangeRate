package com.raffaellmir.exchangerate.presentation.currency

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.PopupMenu
import androidx.annotation.MenuRes
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.raffaellmir.exchangerate.R
import com.raffaellmir.exchangerate.databinding.FragmentCurrencyListBinding
import com.raffaellmir.exchangerate.presentation.helpers.BaseFragment
import com.raffaellmir.exchangerate.util.CurrencyListType.FAVORITE
import com.raffaellmir.exchangerate.util.CurrencyListType.POPULAR
import com.raffaellmir.exchangerate.util.SortType.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CurrencyListFragment : BaseFragment() {
    private var _binding: FragmentCurrencyListBinding? = null
    private val binding get() = _binding!!

    private val currencyViewModel: CurrencyViewModel by activityViewModels()
    private val args: CurrencyListFragmentArgs by navArgs()
    private var currencyAdapter: CurrencyAdapter? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val arrayAdapter =
            ArrayAdapter(requireContext(), R.layout.dropdown_currency_item, listOf("USD", "RUB", "EUR", "LUL", "KEK", "BTC"))
        binding.selectBaseCurrency.setAdapter(arrayAdapter)

        setupCurrencyList()
        setListeners()
        observeOnState()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentCurrencyListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        currencyAdapter = null
        _binding = null
    }

    private fun setupCurrencyList() = with(binding.rvCurrencyList) {
        setHasFixedSize(true)
        currencyAdapter = CurrencyAdapter { currencyViewModel.onClickFavoriteButton(it) }
        layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        adapter = currencyAdapter
    }

    private fun observeOnState() {
        if (args.type == POPULAR) {
            launchFlow {
                currencyViewModel.popularState.collect { currencyAdapter?.submitList(it.currencyList) }
            }
        }

        if (args.type == FAVORITE) {
            launchFlow {
                currencyViewModel.favoriteState.collect { currencyAdapter?.submitList(it.favoriteCurrencyList) }
            }
        }

    }

    private fun setListeners() {
        binding.fabSort.setOnClickListener { v: View ->
            showMenu(v, R.menu.sort_menu)
        }
    }

    private fun showMenu(v: View, @MenuRes menuRes: Int) {
        val popup = PopupMenu(requireContext(), v)
        popup.menuInflater.inflate(menuRes, popup.menu)

        popup.setOnMenuItemClickListener { menuItem: MenuItem ->
            return@setOnMenuItemClickListener when (menuItem.itemId) {
                R.id.option_sort_a_to_z -> currencyViewModel.onSortMenuItemClick(NAME_ASC, args.type)
                R.id.option_sort_z_to_a -> currencyViewModel.onSortMenuItemClick(NAME_DESC, args.type)
                R.id.option_sort_1_to_9 -> currencyViewModel.onSortMenuItemClick(VALUE_ASC, args.type)
                R.id.option_sort_9_to_1 -> currencyViewModel.onSortMenuItemClick(VALUE_DESC, args.type)
                else -> false
            }
        }
        popup.show()
    }
}