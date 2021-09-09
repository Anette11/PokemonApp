package com.example.pokemonapp.model.databasedata

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.pokemonapp.util.PokemonConstants

@Entity(
    tableName = PokemonConstants.POKEMON_TABLE_NAME
)
data class Pokemon(
    @PrimaryKey
    val name: String,
    val front_default: String?,
    val height: String,
    val weight: String
)