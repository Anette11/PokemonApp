package com.example.pokemonapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.pokemonapp.databinding.OneItemBinding
import com.example.pokemonapp.model.wholelistinfo.Result
import javax.inject.Inject

class PokemonPagingDataAdapter
@Inject constructor() :
    PagingDataAdapter<Result, PokemonPagingDataAdapter.PokemonViewHolder>(DiffUtilItemCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokemonViewHolder {
        val binding = OneItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PokemonViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PokemonViewHolder, position: Int) {
        val result = getItem(position)
        result?.let {
            with(holder) {
                bind(result)
                itemView.setOnClickListener { onItemClickShowDetails?.invoke(result) }
            }
        }
    }

    class PokemonViewHolder(private val binding: OneItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(result: Result) {
            with(binding) {
                textViewName.text = result.name
            }
        }
    }

    object DiffUtilItemCallback : DiffUtil.ItemCallback<Result>() {
        override fun areItemsTheSame(oldItem: Result, newItem: Result): Boolean =
            oldItem.name == newItem.name

        override fun areContentsTheSame(oldItem: Result, newItem: Result): Boolean =
            oldItem == newItem
    }

    var onItemClickShowDetails: ((Result) -> Unit)? = null
}