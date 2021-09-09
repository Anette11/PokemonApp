package com.example.pokemonapp.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.pokemonapp.model.databasedata.Pokemon

@Dao
interface PokemonDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun savePokemonToDatabase(pokemon: Pokemon)

    @Delete
    suspend fun deletePokemonFromDatabase(pokemon: Pokemon)

    @Query("SELECT * FROM table_pokemon ORDER BY name ASC")
    fun getWholePokemonListFromDatabase(): LiveData<List<Pokemon>>
}