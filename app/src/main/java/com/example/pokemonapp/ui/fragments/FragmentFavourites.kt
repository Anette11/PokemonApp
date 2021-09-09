package com.example.pokemonapp.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pokemonapp.R
import com.example.pokemonapp.adapter.PokemonListAdapter
import com.example.pokemonapp.databinding.FragmentFavouritesBinding
import com.example.pokemonapp.viewmodel.PokemonViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class FragmentFavourites : Fragment() {
    private var _binding: FragmentFavouritesBinding? = null
    private val binding get() = _binding!!
    private val pokemonViewModel: PokemonViewModel by viewModels()

    @Inject
    lateinit var pokemonListAdapter: PokemonListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFavouritesBinding.inflate(inflater, container, false)
        return binding.apply {
            initializePokemonListAdapter()
            observeWholePokemonListFromDatabase()
            setFragmentTitle()
        }.root.apply {
            setSwipeToDeleteForPokemonListAdapter(this)
        }
    }

    private fun setFragmentTitle() {
        requireActivity().title = getString(R.string.favourite_pokemons)
    }

    private fun initializePokemonListAdapter() {
        with(binding.recyclerView) {
            layoutManager = LinearLayoutManager(requireActivity())
            adapter = pokemonListAdapter
        }
    }

    private fun observeWholePokemonListFromDatabase() {
        pokemonViewModel.wholePokemonListFromDatabase.observe(
            viewLifecycleOwner,
            { wholePokemonList -> pokemonListAdapter.submitList(wholePokemonList) }
        )
    }

    private fun setSwipeToDeleteForPokemonListAdapter(view: View) {
        val itemTouchHelperSimpleCallback =
            object : ItemTouchHelper.SimpleCallback(
                ItemTouchHelper.UP or ItemTouchHelper.DOWN,
                ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
            ) {
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean = true

                override fun onSwiped(
                    viewHolder: RecyclerView.ViewHolder,
                    direction: Int
                ) {
                    val position = viewHolder.absoluteAdapterPosition
                    val pokemon = pokemonListAdapter.currentList[position]
                    pokemonViewModel.deletePokemonFromDatabase(pokemon)

                    Snackbar.make(view, getString(R.string.deleted), Snackbar.LENGTH_LONG)
                        .setAction(getString(R.string.undo)) {
                            pokemonViewModel.savePokemonToDatabase(pokemon)
                        }.show()
                }
            }
        ItemTouchHelper(itemTouchHelperSimpleCallback).attachToRecyclerView(binding.recyclerView)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}