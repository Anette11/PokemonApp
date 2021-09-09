package com.example.pokemonapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.example.pokemonapp.model.databasedata.Pokemon
import com.example.pokemonapp.model.detailedinfo.PokemonDetailedInfoResponse
import com.example.pokemonapp.repository.PokemonRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException
import javax.inject.Inject

@HiltViewModel
class PokemonViewModel
@Inject constructor(
    private val pokemonRepository: PokemonRepository
) : ViewModel() {
    private val _foundPokemonByName = MutableLiveData<PokemonDetailedInfoResponse>()
    val foundPokemonByName: LiveData<PokemonDetailedInfoResponse> get() = _foundPokemonByName

    private val _displayedProgressBarSearchByName = MutableLiveData<Int>()
    val displayedProgressBarSearchByName: LiveData<Int> get() = _displayedProgressBarSearchByName

    val ifShouldShowToastMessageAboutPokemonNotFound = MutableLiveData(false)
    val ifShouldShowSnackBarMessageAboutSocketTimeoutException = MutableLiveData(false)
    val ifShouldMakeUiInvisible = MutableLiveData(true)

    val displayedProgressBarInWebView = MutableLiveData(0)

    val pokemonList = pokemonRepository.getWholePokemonList().cachedIn(viewModelScope)

    val wholePokemonListFromDatabase: LiveData<List<Pokemon>> =
        pokemonRepository.getWholePokemonListFromDatabase()

    fun savePokemonToDatabase(pokemon: Pokemon) = viewModelScope.launch(Dispatchers.IO) {
        pokemonRepository.savePokemonToDatabase(pokemon)
    }

    fun deletePokemonFromDatabase(pokemon: Pokemon) = viewModelScope.launch(Dispatchers.IO) {
        pokemonRepository.deletePokemonFromDatabase(pokemon)
    }

    fun searchPokemonByName(name: String) = viewModelScope.launch(Dispatchers.IO) {
        _displayedProgressBarSearchByName.postValue(1)
        ifShouldMakeUiInvisible.postValue(true)
        try {
            val response = pokemonRepository.searchPokemonByName(name)
            if (response.isSuccessful && response.body() != null) {
                _foundPokemonByName.postValue(response.body())
                ifShouldMakeUiInvisible.postValue(false)
            } else {
                ifShouldShowToastMessageAboutPokemonNotFound.postValue(true)
            }
        } catch (socketTimeoutException: SocketTimeoutException) {
            ifShouldShowSnackBarMessageAboutSocketTimeoutException.postValue(true)
        } finally {
            _displayedProgressBarSearchByName.postValue(0)
        }
        _displayedProgressBarSearchByName.postValue(0)
    }
}