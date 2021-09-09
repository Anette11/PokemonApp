package com.example.pokemonapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.example.pokemonapp.R
import com.example.pokemonapp.databinding.OneItemInFavouritesBinding
import com.example.pokemonapp.model.databasedata.Pokemon
import com.example.pokemonapp.util.PokemonConstants
import java.lang.StringBuilder
import javax.inject.Inject

class PokemonListAdapter @Inject constructor() :
    ListAdapter<Pokemon, PokemonListAdapter.PokemonViewHolder>(PokemonDiffUtilItemCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokemonViewHolder {
        val binding = OneItemInFavouritesBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return PokemonViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: PokemonViewHolder,
        position: Int
    ) {
        with(holder) { bind(getItem(layoutPosition)) }
    }

    class PokemonViewHolder(private val binding: OneItemInFavouritesBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(pokemon: Pokemon) =
            with(binding) {
                textView.text = pokemon.name
                textViewHeight.text = StringBuilder()
                    .append(PokemonConstants.POKEMON_HEIGHT)
                    .append(pokemon.height)
                textViewWeight.text = StringBuilder()
                    .append(PokemonConstants.POKEMON_WEIGHT)
                    .append(pokemon.weight)

                Glide.with(itemView)
                    .load(pokemon.front_default)
                    .apply(RequestOptions.placeholderOf(R.drawable.ic_image_search))
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .error(R.drawable.ic_error)
                    .into(imageView)
            }
    }

    private class PokemonDiffUtilItemCallback : DiffUtil.ItemCallback<Pokemon>() {
        override fun areItemsTheSame(oldItem: Pokemon, newItem: Pokemon): Boolean =
            oldItem.name == newItem.name

        override fun areContentsTheSame(oldItem: Pokemon, newItem: Pokemon): Boolean =
            oldItem.name == newItem.name &&
                    oldItem.front_default == newItem.front_default &&
                    oldItem.height == newItem.height &&
                    oldItem.weight == newItem.weight
    }
}