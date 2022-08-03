package com.raffaellmir.exchangerate.presentation.popular

import android.os.Bundle
import android.view.*
import android.widget.PopupMenu
import androidx.annotation.MenuRes
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.VERTICAL
import com.raffaellmir.exchangerate.R
import com.raffaellmir.exchangerate.databinding.FragmentPopularBinding
import com.raffaellmir.exchangerate.presentation.MainViewModel
import com.raffaellmir.exchangerate.presentation.helpers.BaseFragment
import com.raffaellmir.exchangerate.util.SortType.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PopularFragment : BaseFragment() {
    private var _binding: FragmentPopularBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MainViewModel by viewModels()
    private var currencyAdapter: PopularCurrencyAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPopularBinding.inflate(inflater, container, false)
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

    private fun configureObservers() {
        launchFlow {
            viewModel.popState.collect {
                currencyAdapter?.submitList(it.currencyList)
            }
        }
    }

    private fun configureListeners() {
        binding.fabSort.setOnClickListener { v: View ->
            showMenu(v, R.menu.sort_menu)
        }
    }

    private fun setupCurrencyAdapter() = with(binding.rvPopularCurrency) {
        setHasFixedSize(true)
        currencyAdapter = PopularCurrencyAdapter { viewModel.onClickFavoriteButton(it) }
        layoutManager = LinearLayoutManager(requireContext(), VERTICAL, false)
        adapter = currencyAdapter
    }

    override fun onCreateContextMenu(menu: ContextMenu, v: View, menuInfo: ContextMenu.ContextMenuInfo?) {
        super.onCreateContextMenu(menu, v, menuInfo)
        val popup = PopupMenu(requireContext(), v)
        val inflater: MenuInflater = popup.menuInflater
        inflater.inflate(R.menu.sort_menu, menu)
    }


    private fun showMenu(v: View, @MenuRes menuRes: Int) {
        val popup = PopupMenu(requireContext(), v)
        popup.menuInflater.inflate(menuRes, popup.menu)

        popup.setOnMenuItemClickListener { menuItem: MenuItem ->
            return@setOnMenuItemClickListener when (menuItem.itemId) {
                R.id.option_sort_a_to_z -> viewModel.getSortedCurrencyList(NAME_ASC)
                R.id.option_sort_z_to_a -> viewModel.getSortedCurrencyList(NAME_DESC)
                R.id.option_sort_1_to_9 -> viewModel.getSortedCurrencyList(VALUE_ASC)
                R.id.option_sort_9_to_1 -> viewModel.getSortedCurrencyList(VALUE_DESC)
                else -> false
            }
        }
        popup.show()
    }
}