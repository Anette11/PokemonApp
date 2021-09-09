package com.example.pokemonapp.di

import android.content.Context
import androidx.room.Room
import com.example.pokemonapp.api.PokemonWebservice
import com.example.pokemonapp.database.PokemonDao
import com.example.pokemonapp.database.PokemonDatabase
import com.example.pokemonapp.util.PokemonConstants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object PokemonModule {
    @Provides
    @Singleton
    fun provideRetrofitInstance(): Retrofit =
        Retrofit.Builder()
            .baseUrl(PokemonConstants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Provides
    @Singleton
    fun providePokemonWebservice(retrofit: Retrofit): PokemonWebservice =
        retrofit.create(PokemonWebservice::class.java)

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): PokemonDatabase =
        Room.databaseBuilder(
            context,
            PokemonDatabase::class.java,
            PokemonConstants.POKEMON_DATABASE_NAME
        ).build()

    @Provides
    fun providePokemonDao(pokemonDatabase: PokemonDatabase): PokemonDao =
        pokemonDatabase.getPokemonDao()
}