package com.raffaellmir.exchangerate.presentation.popular

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.VERTICAL
import com.raffaellmir.exchangerate.databinding.FragmentPopularBinding
import com.raffaellmir.exchangerate.presentation.MainViewModel
import com.raffaellmir.exchangerate.presentation.helpers.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PopularFragment : BaseFragment() {
    private var _binding: FragmentPopularBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MainViewModel by viewModels()

    private val currencyAdapter = PopularCurrencyAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPopularBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        configureViews()
        initObservers()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun configureViews() {
        setupCurrencyAdapter()
    }

    private fun initObservers() {
        launchFlow {
            viewModel.popState.collect {
                currencyAdapter.submitList(it.currencyList)
            }
        }
    }

    private fun setupCurrencyAdapter() = with(binding.rvPopularCurrency) {
        layoutManager = LinearLayoutManager(requireContext(), VERTICAL, false)
        adapter = currencyAdapter
    }
}