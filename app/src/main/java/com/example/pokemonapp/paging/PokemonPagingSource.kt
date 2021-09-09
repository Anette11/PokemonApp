package com.example.pokemonapp.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.pokemonapp.api.PokemonWebservice
import com.example.pokemonapp.model.wholelistinfo.Result
import com.example.pokemonapp.util.PokemonConstants
import retrofit2.HttpException
import java.io.IOException

class PokemonPagingSource(
    private val pokemonWebservice: PokemonWebservice
) : PagingSource<Int, Result>() {

    override suspend fun load(
        params: LoadParams<Int>
    ): LoadResult<Int, Result> =
        try {
            val offset = params.key ?: PokemonConstants.POKEMON_OFFSET_DEFAULT_VALUE

            val pokemonResponse = pokemonWebservice.getWholePokemonList(
                PokemonConstants.POKEMON_LIMIT_DEFAULT_VALUE,
                offset
            )
            val listOfResults = pokemonResponse.results

            LoadResult.Page(
                data = listOfResults,
                prevKey = if (offset == PokemonConstants.POKEMON_OFFSET_DEFAULT_VALUE) null
                else offset.minus(PokemonConstants.POKEMON_LIMIT_DEFAULT_VALUE),
                nextKey = if (listOfResults.isEmpty()) null
                else offset.plus(PokemonConstants.POKEMON_LIMIT_DEFAULT_VALUE)
            )
        } catch (ioException: IOException) {
            LoadResult.Error(ioException)
        } catch (httpException: HttpException) {
            LoadResult.Error(httpException)
        }

    override fun getRefreshKey(
        state: PagingState<Int, Result>
    ): Int? =
        state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(PokemonConstants.POKEMON_LIMIT_DEFAULT_VALUE)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(PokemonConstants.POKEMON_LIMIT_DEFAULT_VALUE)
        }
}