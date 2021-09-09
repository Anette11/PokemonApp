package com.example.pokemonapp.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.pokemonapp.adapter.PokemonLoadStateAdapter
import com.example.pokemonapp.adapter.PokemonPagingDataAdapter
import com.example.pokemonapp.databinding.FragmentHomeBinding
import com.example.pokemonapp.viewmodel.PokemonViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class FragmentHome : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val pokemonViewModel: PokemonViewModel by viewModels()

    @Inject
    lateinit var pokemonPagingDataAdapter: PokemonPagingDataAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.apply {
            binding.progressBar.visibility = View.VISIBLE
            submitPagingDataToPokemonPagingDataAdapter()
            initializeRecyclerView()
        }.root
    }

    private fun submitPagingDataToPokemonPagingDataAdapter() {
        lifecycleScope.launch {
            pokemonViewModel.pokemonList.collectLatest { pagingDataResult ->
                binding.progressBar.visibility = View.GONE
                pokemonPagingDataAdapter.submitData(pagingDataResult)
            }
        }
    }

    private fun initializeRecyclerView() {
        binding.recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = GridLayoutManager(requireActivity(), 2)
            adapter = pokemonPagingDataAdapter.withLoadStateHeaderAndFooter(
                header = PokemonLoadStateAdapter { pokemonPagingDataAdapter.retry() },
                footer = PokemonLoadStateAdapter { pokemonPagingDataAdapter.retry() }
            )
            pokemonPagingDataAdapter.onItemClickShowDetails = { result ->
                val action = FragmentHomeDirections.actionFragmentHomeToFragmentDetails(result)
                findNavController().navigate(action)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}