package com.raffaellmir.exchangerate.presentation.currency

import android.os.Bundle
import android.text.InputFilter
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView.NOT_FOCUSABLE
import android.widget.AdapterView.OnItemClickListener
import android.widget.ArrayAdapter
import android.widget.PopupMenu
import androidx.annotation.MenuRes
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.VERTICAL
import com.raffaellmir.exchangerate.R
import com.raffaellmir.exchangerate.R.layout.dropdown_currency_item
import com.raffaellmir.exchangerate.databinding.FragmentCurrencyListBinding
import com.raffaellmir.exchangerate.presentation.helpers.BaseFragment
import com.raffaellmir.exchangerate.util.SortType.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class CurrencyListFragment : BaseFragment() {
    private var _binding: FragmentCurrencyListBinding? = null
    private val binding get() = _binding!!

    private val currencyViewModel: CurrencyViewModel by viewModels()
    private val args: CurrencyListFragmentArgs by navArgs()
    private var currencyAdapter: CurrencyAdapter? = null
    private var baseCurrencyAdapter: ArrayAdapter<String>? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
        currencyViewModel.onListChange(args.type)
        currencyAdapter = CurrencyAdapter(currencyViewModel::onFavoriteChange)
        layoutManager = LinearLayoutManager(requireContext(), VERTICAL, false)
        adapter = currencyAdapter
    }

    private fun observeOnState() {
        launchFlow {
            currencyViewModel.currencyState.collect { state ->
                currencyAdapter?.submitList(state.currencyList)
            }
        }

        launchFlow {
            currencyViewModel.baseCurrencyList.collect { list ->
                setupBaseCurrencyList(list)
            }
        }

        launchFlow {
            currencyViewModel.baseCurrency.collect{ symbol ->
                binding.baseCurrencyList.setText(symbol)
                currencyViewModel.onBaseCurrencyChange(symbol)
            }
        }
    }

    private fun setListeners() = with(binding) {
        fabSort.setOnClickListener { v: View ->
            showMenu(v, R.menu.sort_menu)
        }

        baseCurrencyList.onItemClickListener = OnItemClickListener { parent, _, position, _ ->
            val symbol = parent.getItemAtPosition(position).toString()
            baseCurrencyList.focusable = NOT_FOCUSABLE
            currencyViewModel.onBaseCurrencyChange(symbol)
        }
    }

    private fun setupBaseCurrencyList(currencySymbolList: List<String>) = with(binding.baseCurrencyList) {
        baseCurrencyAdapter = ArrayAdapter(requireContext(), dropdown_currency_item, currencySymbolList)
        setAdapter(baseCurrencyAdapter)
        filters += InputFilter.AllCaps()
    }

    private fun showMenu(v: View, @MenuRes menuRes: Int) {
        val popup = PopupMenu(requireContext(), v)
        popup.menuInflater.inflate(menuRes, popup.menu)

        popup.setOnMenuItemClickListener { menuItem: MenuItem ->
            return@setOnMenuItemClickListener when (menuItem.itemId) {
                R.id.option_sort_a_to_z -> currencyViewModel.onSortChange(NAME_ASC)
                R.id.option_sort_z_to_a -> currencyViewModel.onSortChange(NAME_DESC)
                R.id.option_sort_1_to_9 -> currencyViewModel.onSortChange(VALUE_ASC)
                R.id.option_sort_9_to_1 -> currencyViewModel.onSortChange(VALUE_DESC)
                else -> false
            }
        }
        popup.show()
    }
}