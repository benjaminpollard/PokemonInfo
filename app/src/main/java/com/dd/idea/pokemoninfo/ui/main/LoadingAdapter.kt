package com.dd.idea.pokemoninfo.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.dd.idea.pokemoninfo.databinding.LayoutLoadingBinding

class LoadingAdapter(
    private val adapter: PokemonAdapter
) : LoadStateAdapter<LoadingAdapter.NetworkStateItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState) =
        NetworkStateItemViewHolder(
            LayoutLoadingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        ) { adapter.retry() }

    override fun onBindViewHolder(holder: NetworkStateItemViewHolder, loadState: LoadState) =
        holder.bind(loadState)

    class NetworkStateItemViewHolder(
        private val binding: LayoutLoadingBinding,
        private val retryCallback: () -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.retryButton.setOnClickListener { retryCallback() }
        }

        fun bind(loadState: LoadState) {
            with(binding) {
                progressBar.isVisible = loadState is LoadState.Loading
                retryButton.isVisible = loadState is LoadState.Error
                errorMsg.isVisible =
                    !(loadState as? LoadState.Error)?.error?.message.isNullOrBlank()
                errorMsg.text = (loadState as? LoadState.Error)?.error?.message
            }
        }
    }
}