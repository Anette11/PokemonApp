package com.example.pokemonapp.model.detailedinfo

data class PokemonDetailedInfoResponse(
    val height: Int,
    val name: String,
    val sprites: Sprites,
    val weight: Int
)