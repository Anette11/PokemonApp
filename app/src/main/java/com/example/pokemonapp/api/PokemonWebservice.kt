package com.example.pokemonapp.api

import com.example.pokemonapp.model.detailedinfo.PokemonDetailedInfoResponse
import com.example.pokemonapp.model.wholelistinfo.PokemonWholeListResponse
import com.example.pokemonapp.util.PokemonConstants
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PokemonWebservice {
    @GET("pokemon/")
    suspend fun getWholePokemonList(
        @Query(PokemonConstants.LIMIT) limit: Int,
        @Query(PokemonConstants.OFFSET) offset: Int
    ): PokemonWholeListResponse

    // name or id of pokemon in String format
    @GET("pokemon/{name}/")
    suspend fun searchPokemonByName(
        @Path("name") name: String
    ): Response<PokemonDetailedInfoResponse>
}