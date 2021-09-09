package com.example.pokemonapp.ui.fragments

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.example.pokemonapp.R
import com.example.pokemonapp.databinding.FragmentSearchByNameBinding
import com.example.pokemonapp.model.databasedata.Pokemon
import com.example.pokemonapp.util.PokemonConstants
import com.example.pokemonapp.viewmodel.PokemonViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FragmentSearchRandom : Fragment() {
    private var _binding: FragmentSearchByNameBinding? = null
    private val binding get() = _binding!!
    private val pokemonViewModel: PokemonViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSearchByNameBinding.inflate(inflater, container, false)
        return binding.apply {
            fillLayout()
            setHasOptionsMenu(true)
            changeUiVisibility()
            changeProgressBarVisibility()
            checkIfShouldShowToastMessageAboutPokemonNotFound()
            checkIfShouldShowSnackBarMessageAboutSocketTimeoutException()
            floatingActionButtonSetOnClickListener(floatingActionButton)
            setFragmentTitle()
        }.root
    }

    private fun setFragmentTitle() {
        requireActivity().title = getString(R.string.search_random_pokemon)
    }

    private fun findRandomPokemonId(): Int {
//  1-898 (1 part of id numbers)
//  10001-10220 (2 part of id numbers)
//  first step: generate number in interval [1;2]
//  second step: if number from first step is 1 then generate number from interval [1;898] else - [10001;10220]
        val randomNumberFirstStep = 1 + (Math.random() * 2).toInt()

        val randomNumberSecondStep = if (randomNumberFirstStep == 1) {
            1 + (Math.random() * 898).toInt()
        } else {
            10001 + (Math.random() * 220).toInt()
        }
        return randomNumberSecondStep
    }

    private fun fillLayout() {
        pokemonViewModel.foundPokemonByName.observe(viewLifecycleOwner, {
            it?.let {
                with(binding) {
                    textView.text = it.name
                    textViewHeight.text = StringBuilder()
                        .append(PokemonConstants.POKEMON_HEIGHT)
                        .append(it.height)
                    textViewWeight.text = StringBuilder()
                        .append(PokemonConstants.POKEMON_WEIGHT)
                        .append(it.weight)

                    Glide.with(this@FragmentSearchRandom)
                        .load(it.sprites.other.official_artwork.front_default)
                        .apply(RequestOptions.placeholderOf(R.drawable.ic_image_search))
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .error(R.drawable.ic_error)
                        .into(imageView)
                }
            }
        })
    }

    private fun changeUiVisibility() {
        pokemonViewModel.ifShouldMakeUiInvisible.observe(viewLifecycleOwner, {
            if (it) makeUiInvisible()
            else makeUiVisible()
        })
    }

    private fun makeUiVisible() {
        with(binding) {
            textView.visibility = View.VISIBLE
            textViewHeight.visibility = View.VISIBLE
            textViewWeight.visibility = View.VISIBLE
            imageView.visibility = View.VISIBLE
            floatingActionButton.visibility = View.VISIBLE
        }
    }

    private fun makeUiInvisible() {
        with(binding) {
            textView.visibility = View.INVISIBLE
            textViewHeight.visibility = View.INVISIBLE
            textViewWeight.visibility = View.INVISIBLE
            imageView.visibility = View.INVISIBLE
            floatingActionButton.visibility = View.GONE
        }
    }

    private fun floatingActionButtonSetOnClickListener(
        floatingActionButton: FloatingActionButton
    ) {
        floatingActionButton.setOnClickListener {
            val response = pokemonViewModel.foundPokemonByName.value
            response?.let {
                val pokemon = Pokemon(
                    name = it.name,
                    front_default = it.sprites.other.official_artwork.front_default,
                    height = it.height.toString(),
                    weight = it.weight.toString()
                )
                pokemonViewModel.savePokemonToDatabase(pokemon)
                showToastMessage(getString(R.string.saved))
            }
        }
    }

    private fun showToastMessage(message: String) {
        Toast.makeText(requireActivity(), message, Toast.LENGTH_SHORT).show()
    }

    private fun changeProgressBarVisibility() {
        pokemonViewModel.displayedProgressBarSearchByName.observe(viewLifecycleOwner, {
            with(binding) {
                if (it == 1) progressBar.visibility = View.VISIBLE
                else progressBar.visibility = View.GONE
            }
        })
    }

    override fun onCreateOptionsMenu(
        menu: Menu,
        menuInflater: MenuInflater
    ) {
        super.onCreateOptionsMenu(menu, menuInflater)

        menuInflater.inflate(R.menu.menu_search_random, menu)
        val menuItemSearch = menu.findItem(R.id.menu_item_search)

        menuItemSearch.setOnMenuItemClickListener {
            val pokemonId = findRandomPokemonId().toString()
            pokemonViewModel.searchPokemonByName(pokemonId)
            true
        }
    }

    private fun checkIfShouldShowToastMessageAboutPokemonNotFound() {
        pokemonViewModel.ifShouldShowToastMessageAboutPokemonNotFound.observe(viewLifecycleOwner, {
            if (it) {
                pokemonViewModel.ifShouldShowToastMessageAboutPokemonNotFound.postValue(false)
                showToastMessage(getString(R.string.not_found))
            }
        })
    }

    private fun checkIfShouldShowSnackBarMessageAboutSocketTimeoutException() {
        pokemonViewModel.ifShouldShowSnackBarMessageAboutSocketTimeoutException.observe(
            viewLifecycleOwner,
            {
                if (it) {
                    pokemonViewModel.ifShouldShowSnackBarMessageAboutSocketTimeoutException.postValue(
                        false
                    )
                    showToastMessage(getString(R.string.timeout_exception_try_again))
                }
            })
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}