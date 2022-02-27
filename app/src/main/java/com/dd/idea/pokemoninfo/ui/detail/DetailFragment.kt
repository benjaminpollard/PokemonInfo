package com.dd.idea.pokemoninfo.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.dd.idea.pokemoninfo.R
import com.dd.idea.pokemoninfo.databinding.DetailFragmentBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.util.*

class DetailFragment : Fragment() {

    private val viewModel: DetailViewModel by viewModel { parametersOf(arguments) }

    private lateinit var binding: DetailFragmentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycle.addObserver(viewModel)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = DetailFragmentBinding.inflate(inflater)

        setUpOnClicks()
        setUpObservers()

        return binding.root
    }

    private fun setUpOnClicks() {
        binding.back.setOnClickListener {
            viewModel.onBackPressed()
        }
        binding.retry.setOnClickListener {
            viewModel.onRetryPressed()
        }
    }

    private fun setUpObservers() {

        viewModel.apply {

            pokemonNameLiveData.observe(viewLifecycleOwner) {
                binding.nameHolder.isVisible = true
                binding.name.text = it.capitalize(Locale.ROOT)
            }

            pokemonDetailLiveData.observe(viewLifecycleOwner) {
                binding.apply {

                    Glide.with(this@DetailFragment.requireContext()).load(it.image).into(image)

                    binding.name.text = it.name.capitalize(Locale.ROOT)

                    binding.height.text = getString(R.string.detail_height, it.height)
                    binding.weight.text = getString(R.string.detail_weight, it.weight)
                    binding.type.text = getString(R.string.detail_type, it.type)

                }
            }

            toastLiveData.observe(viewLifecycleOwner) {
                it?.getContentIfNotHandled()?.let { message ->
                    Toast.makeText(
                        this@DetailFragment.requireContext(),
                        message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            popBackStackLiveData.observe(viewLifecycleOwner, {
                it?.getContentIfNotHandled()?.let {
                    requireActivity().supportFragmentManager.popBackStack()
                }
            })

            stateLiveData.observe(viewLifecycleOwner, {
                binding.apply {
                    val showContent = it == DetailViewModel.State.COMPLETE
                    val showLoading = it == DetailViewModel.State.LOADING
                    val showTryAgain = it == DetailViewModel.State.ERROR

                    loading.isVisible = showLoading
                    image.isVisible = showContent
                    binding.nameHolder.isVisible = showContent
                    binding.detailsHolder.isVisible = showContent
                    retry.isVisible = showTryAgain
                }
            })
        }
    }

    companion object {
        fun newInstance(name: String, url: String) = DetailFragment().apply {
            val args = Bundle()
            args.putString(DetailViewModel.NAME, name)
            args.putString(DetailViewModel.URL, url)
            arguments = args
        }
    }
}