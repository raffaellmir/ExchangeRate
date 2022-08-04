package com.raffaellmir.exchangerate.presentation.currency.popular

import android.os.Bundle
import android.view.*
import android.widget.PopupMenu
import androidx.annotation.MenuRes
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.VERTICAL
import com.raffaellmir.exchangerate.R
import com.raffaellmir.exchangerate.databinding.FragmentPopularBinding
import com.raffaellmir.exchangerate.presentation.currency.CurrencyAdapter
import com.raffaellmir.exchangerate.presentation.MainViewModel
import com.raffaellmir.exchangerate.presentation.helpers.BaseFragment
import com.raffaellmir.exchangerate.util.SortType.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PopularFragment : BaseFragment() {
    private var _binding: FragmentPopularBinding? = null
    private val binding get() = _binding!!

    private val mainViewModel: MainViewModel by viewModels()
    private var currencyAdapter: CurrencyAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPopularBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupCurrencyList()
        setListeners()
        observeOnState()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        currencyAdapter = null
        _binding = null
    }

    private fun observeOnState() {
        launchFlow {
            mainViewModel.popularState.collect {
                currencyAdapter?.submitList(it.currencyList)
            }
        }
    }

    private fun setListeners() {
        binding.fabSort.setOnClickListener { v: View ->
            showMenu(v, R.menu.sort_menu)
        }
    }

    private fun setupCurrencyList() = with(binding.rvPopularCurrency) {
        mainViewModel.getPopularCurrencyList(mainViewModel.popularState.value.sortType)

        setHasFixedSize(true)
        currencyAdapter = CurrencyAdapter { mainViewModel.onClickFavoriteButton(it) }
        layoutManager = LinearLayoutManager(requireContext(), VERTICAL, false)
        adapter = currencyAdapter
    }

    private fun showMenu(v: View, @MenuRes menuRes: Int) {
        val popup = PopupMenu(requireContext(), v)
        popup.menuInflater.inflate(menuRes, popup.menu)

        popup.setOnMenuItemClickListener { menuItem: MenuItem ->
            return@setOnMenuItemClickListener when (menuItem.itemId) {
                R.id.option_sort_a_to_z -> mainViewModel.getPopularCurrencyList(NAME_ASC)
                R.id.option_sort_z_to_a -> mainViewModel.getPopularCurrencyList(NAME_DESC)
                R.id.option_sort_1_to_9 -> mainViewModel.getPopularCurrencyList(VALUE_ASC)
                R.id.option_sort_9_to_1 -> mainViewModel.getPopularCurrencyList(VALUE_DESC)
                else -> false
            }
        }
        popup.show()
    }
}