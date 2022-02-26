package com.dd.idea.pokemoninfo.ui.comparator

import androidx.recyclerview.widget.DiffUtil
import com.dd.idea.pokemoninfo.models.Pokemon

object PokemonComparator : DiffUtil.ItemCallback<Pokemon>() {
    override fun areItemsTheSame(oldItem: Pokemon, newItem: Pokemon): Boolean {
        return oldItem.name == newItem.name
    }

    override fun areContentsTheSame(oldItem: Pokemon, newItem: Pokemon): Boolean {
        return oldItem.url == newItem.url
    }
}