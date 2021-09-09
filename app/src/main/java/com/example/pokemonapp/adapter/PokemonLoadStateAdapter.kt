package com.example.pokemonapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.pokemonapp.databinding.LoadStateFooterBinding

class PokemonLoadStateAdapter(private val retry: () -> Unit) :
    LoadStateAdapter<PokemonLoadStateAdapter.PokemonLoadStateViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ): PokemonLoadStateViewHolder {
        val binding = LoadStateFooterBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return PokemonLoadStateViewHolder(binding).also {
            binding.buttonRetry.setOnClickListener { retry() }
        }
    }

    override fun onBindViewHolder(
        holder: PokemonLoadStateViewHolder,
        loadState: LoadState
    ) {
        holder.bind(loadState)
    }

    class PokemonLoadStateViewHolder(
        private val binding: LoadStateFooterBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(loadState: LoadState) =
            binding.apply {
                progressBarTryAgain.isVisible = loadState is LoadState.Loading
                buttonRetry.isVisible = loadState !is LoadState.Loading
                textViewTryAgain.isVisible = loadState !is LoadState.Loading
            }
    }
}