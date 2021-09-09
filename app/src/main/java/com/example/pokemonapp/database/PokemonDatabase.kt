package com.example.pokemonapp.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.pokemonapp.model.databasedata.Pokemon

@Database(
    entities = [Pokemon::class],
    version = 1,
    exportSchema = false
)
abstract class PokemonDatabase : RoomDatabase() {
    abstract fun getPokemonDao(): PokemonDao
}