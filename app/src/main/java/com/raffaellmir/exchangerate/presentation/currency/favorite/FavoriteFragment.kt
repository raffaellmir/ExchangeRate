package com.raffaellmir.exchangerate.presentation.currency.favorite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.annotation.MenuRes
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.VERTICAL
import com.raffaellmir.exchangerate.R
import com.raffaellmir.exchangerate.databinding.FragmentFavoriteBinding
import com.raffaellmir.exchangerate.presentation.MainViewModel
import com.raffaellmir.exchangerate.presentation.currency.CurrencyAdapter
import com.raffaellmir.exchangerate.presentation.helpers.BaseFragment
import com.raffaellmir.exchangerate.util.SortType.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoriteFragment : BaseFragment() {
    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!!

    private val mainViewModel: MainViewModel by viewModels()
    private val favoriteViewModel: FavoriteViewModel by viewModels()
    private var currencyAdapter: CurrencyAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        configureViews()
        configureListeners()
        configureObservers()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        currencyAdapter = null
        _binding = null
    }

    private fun configureViews() {
        setupCurrencyAdapter()
    }

    private fun configureListeners() {
        binding.fabSort.setOnClickListener { v: View ->
            showMenu(v, R.menu.sort_menu)
        }
    }

    private fun configureObservers() {
        launchFlow {
            favoriteViewModel.favoriteState.collect {
                currencyAdapter?.submitList(it.currencyList)
            }
        }
    }

    private fun setupCurrencyAdapter() = with(binding.rvFavoriteCurrency) {
        //todo обзервить изменения списка
        favoriteViewModel.getCurrencyList(favoriteViewModel.favoriteState.value.sortType)

        setHasFixedSize(true)
        currencyAdapter = CurrencyAdapter { favoriteViewModel.onClickFavoriteButton(it) }
        layoutManager = LinearLayoutManager(requireContext(), VERTICAL, false)
        adapter = currencyAdapter
    }

    private fun showMenu(v: View, @MenuRes menuRes: Int) {
        val popup = PopupMenu(requireContext(), v)
        popup.menuInflater.inflate(menuRes, popup.menu)

        popup.setOnMenuItemClickListener { menuItem: MenuItem ->
            return@setOnMenuItemClickListener when (menuItem.itemId) {
                R.id.option_sort_a_to_z -> favoriteViewModel.getCurrencyList(NAME_ASC)
                R.id.option_sort_z_to_a -> favoriteViewModel.getCurrencyList(NAME_DESC)
                R.id.option_sort_1_to_9 -> favoriteViewModel.getCurrencyList(VALUE_ASC)
                R.id.option_sort_9_to_1 -> favoriteViewModel.getCurrencyList(VALUE_DESC)
                else -> false
            }
        }
        popup.show()
    }
}