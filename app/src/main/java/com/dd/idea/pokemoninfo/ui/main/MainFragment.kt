package com.dd.idea.pokemoninfo.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.dd.idea.pokemoninfo.R
import com.dd.idea.pokemoninfo.databinding.MainFragmentBinding
import com.dd.idea.pokemoninfo.models.Pokemon
import com.dd.idea.pokemoninfo.ui.detail.DetailFragment
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainFragment : Fragment() {

    private var adapter: PokemonAdapter? = null
    private val viewModel: MainViewModel by viewModel()
    private lateinit var binding: MainFragmentBinding

    companion object {
        fun newInstance() = MainFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycle.addObserver(viewModel)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = MainFragmentBinding.inflate(inflater).apply {

            adapter = PokemonAdapter(object : PokemonAdapter.OnItemClick {
                override fun selected(pokemon: Pokemon) {
                    viewModel.onPokemonSelected(pokemon)
                }
            })
            pokemonList.layoutManager = LinearLayoutManager(this@MainFragment.requireContext())

            pokemonList.adapter = adapter?.withLoadStateFooter(LoadingAdapter(adapter!!))

            swiperefresh.setOnRefreshListener {
                adapter?.refresh()

            }
        }

        setUpObservers()

        return binding.root
    }

    private fun setUpObservers() {

        viewModel.apply {

            viewModel.pokemonListLiveData.observe(viewLifecycleOwner) {
                binding.loading.isVisible = false
                binding.swiperefresh.isRefreshing = false
                lifecycleScope.launch {
                    adapter?.submitData(it)
                }
            }

            showPokemonDetailLiveData.observe(viewLifecycleOwner) {
                it.getContentIfNotHandled()?.let { pokemon ->
                    requireActivity().supportFragmentManager.beginTransaction()
                        .add(R.id.container, DetailFragment.newInstance(pokemon.name, pokemon.url))
                        .addToBackStack(DetailFragment::class.java.simpleName)
                        .commit()
                }
            }

        }
    }

}