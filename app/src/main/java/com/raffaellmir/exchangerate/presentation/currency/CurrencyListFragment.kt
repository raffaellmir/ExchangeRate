package com.raffaellmir.exchangerate.presentation.currency

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.annotation.MenuRes
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.raffaellmir.exchangerate.R
import com.raffaellmir.exchangerate.databinding.FragmentCurrencyListBinding
import com.raffaellmir.exchangerate.presentation.CurrencyViewModel
import com.raffaellmir.exchangerate.presentation.helpers.BaseFragment
import com.raffaellmir.exchangerate.util.SortType
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CurrencyListFragment : BaseFragment() {
    private var _binding: FragmentCurrencyListBinding? = null
    private val binding get() = _binding!!

    private val currencyViewModel: CurrencyViewModel by viewModels()
    private val args: CurrencyListFragmentArgs by navArgs()
    private var currencyAdapter: CurrencyAdapter? = null

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
        currencyAdapter = CurrencyAdapter { currencyViewModel.onClickFavoriteButton(it) }
        layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        adapter = currencyAdapter
    }

    private fun observeOnState() {
        launchFlow {
            currencyViewModel.popularState.collect {
                currencyAdapter?.submitList(it.currencyList)
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
                R.id.option_sort_a_to_z -> currencyViewModel.getPopularCurrencyList(SortType.NAME_ASC)
                R.id.option_sort_z_to_a -> currencyViewModel.getPopularCurrencyList(SortType.NAME_DESC)
                R.id.option_sort_1_to_9 -> currencyViewModel.getPopularCurrencyList(SortType.VALUE_ASC)
                R.id.option_sort_9_to_1 -> currencyViewModel.getPopularCurrencyList(SortType.VALUE_DESC)
                else -> false
            }
        }
        popup.show()
    }
}