package com.raffaellmir.exchangerate.presentation.popular

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.HORIZONTAL
import com.raffaellmir.exchangerate.data.network.api.CurrencyApiRepository
import com.raffaellmir.exchangerate.databinding.FragmentPopularBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class PopularFragment : Fragment() {
    private var _binding: FragmentPopularBinding? = null
    private val binding get() = _binding!!

    @Inject lateinit var currencyApiRepository: CurrencyApiRepository
    private var currencyRatesAdapter: PopularRatesAdapter? = PopularRatesAdapter(emptyList())

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPopularBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupCurrencyRatesAdapter()
        initObservers()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        currencyRatesAdapter = null
        _binding = null
    }

    private fun initObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {

                currencyApiRepository.getExchangeRateBasedOn("USD").collect { response ->
                    binding.tvCurrency.text = response.rates["RUB"].toString()
                }
            }
        }
    }

    private fun setupCurrencyRatesAdapter() = with(binding.rvPopularCurrency) {
        layoutManager = LinearLayoutManager(requireContext(), HORIZONTAL, false)
        adapter = currencyRatesAdapter
    }
}