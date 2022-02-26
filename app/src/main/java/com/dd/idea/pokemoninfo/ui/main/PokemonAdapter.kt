package com.dd.idea.pokemoninfo.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.dd.idea.pokemoninfo.databinding.ItemPokemonBinding
import com.dd.idea.pokemoninfo.models.Pokemon
import com.dd.idea.pokemoninfo.ui.comparator.PokemonComparator
import java.util.*

class PokemonAdapter(var onItemClick: OnItemClick) :
    PagingDataAdapter<Pokemon, PokemonAdapter.PokemonViewHolder>(PokemonComparator) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokemonViewHolder {
        val binding = ItemPokemonBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PokemonViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PokemonViewHolder, position: Int) {
        val binding = holder.binding

        binding.pokemonName.text = getItem(position)?.name?.capitalize(Locale.ROOT)
        binding.root.setOnClickListener {
            getItem(position)?.let { it1 -> onItemClick.selected(it1) }
        }
    }

    class PokemonViewHolder(val binding: ItemPokemonBinding) : RecyclerView.ViewHolder(binding.root)

    interface OnItemClick {
        fun selected(pokemon: Pokemon)
    }

}