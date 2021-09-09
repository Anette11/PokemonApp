package com.example.pokemonapp.model.detailedinfo

import com.google.gson.annotations.SerializedName

data class Other(
    @SerializedName("official-artwork")
    val official_artwork: OfficialArtwork
)