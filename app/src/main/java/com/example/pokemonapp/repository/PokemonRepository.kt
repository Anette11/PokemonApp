package com.example.pokemonapp.repository

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.example.pokemonapp.api.PokemonWebservice
import com.example.pokemonapp.database.PokemonDao
import com.example.pokemonapp.model.databasedata.Pokemon
import com.example.pokemonapp.paging.PokemonPagingSource
import com.example.pokemonapp.util.PokemonConstants
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PokemonRepository
@Inject constructor(
    private val pokemonWebservice: PokemonWebservice,
    private val pokemonDao: PokemonDao
) {
    fun getWholePokemonList() =
        Pager(
            config = PagingConfig(
                pageSize = PokemonConstants.POKEMON_LIMIT_DEFAULT_VALUE,
                maxSize = PokemonConstants.POKEMON_MAX_SIZE_VALUE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                PokemonPagingSource(pokemonWebservice)
            }
        ).flow

    suspend fun searchPokemonByName(name: String) =
        pokemonWebservice.searchPokemonByName(name)

    suspend fun savePokemonToDatabase(pokemon: Pokemon) =
        pokemonDao.savePokemonToDatabase(pokemon)

    suspend fun deletePokemonFromDatabase(pokemon: Pokemon) =
        pokemonDao.deletePokemonFromDatabase(pokemon)

    fun getWholePokemonListFromDatabase(): LiveData<List<Pokemon>> =
        pokemonDao.getWholePokemonListFromDatabase()
}