package com.dd.idea.pokemoninfo.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dd.idea.pokemoninfo.databinding.ItemPokemonBinding
import com.dd.idea.pokemoninfo.models.Pokemon
import java.util.*

class PokemonAdapter(var onItemClick: OnItemClick) :
    RecyclerView.Adapter<PokemonAdapter.PokemonViewHolder>() {

    var itemList: MutableList<Pokemon> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokemonViewHolder {
        val binding = ItemPokemonBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PokemonViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PokemonViewHolder, position: Int) {
        val binding = holder.binding
        val item = itemList[position]
        binding.pokemonName.text = item.name.capitalize(Locale.ROOT)
        binding.root.setOnClickListener {
            item.let { it1 -> onItemClick.selected(it1) }
        }
    }

    fun addItems(it: List<Pokemon>?) {
        if (it != null) {
            itemList.addAll(it)
            notifyDataSetChanged()
        }
    }

    class PokemonViewHolder(val binding: ItemPokemonBinding) : RecyclerView.ViewHolder(binding.root)

    interface OnItemClick {
        fun selected(pokemon: Pokemon)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

}