package com.example.pokemonapp.ui.fragments

import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.pokemonapp.R
import com.example.pokemonapp.databinding.FragmentWebviewBinding
import com.example.pokemonapp.util.PokemonConstants
import com.example.pokemonapp.viewmodel.PokemonViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FragmentWebView : Fragment() {
    private var _binding: FragmentWebviewBinding? = null
    private val binding get() = _binding!!
    private val pokemonViewModel: PokemonViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentWebviewBinding.inflate(inflater, container, false)
        return binding.apply {
            changeProgressBarVisibility()
            setFragmentTitle()
            setWebView(this)
            letWebViewGoBackOnBackPressedButton()
        }.root
    }

    private fun setFragmentTitle() {
        requireActivity().title = getString(R.string.about_api)
    }

    private fun changeProgressBarVisibility() {
        pokemonViewModel.displayedProgressBarInWebView.observe(viewLifecycleOwner, {
            if (it == 1) binding.progressBar.visibility = View.VISIBLE
            else binding.progressBar.visibility = View.GONE
        })
    }

    private fun setWebView(binding: FragmentWebviewBinding) {
        binding.webView.apply {
            webViewClient = object : WebViewClient() {
                override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                    super.onPageStarted(view, url, favicon)
                    pokemonViewModel.displayedProgressBarInWebView.postValue(1)
                }

                override fun onPageCommitVisible(view: WebView?, url: String?) {
                    super.onPageCommitVisible(view, url)
                    pokemonViewModel.displayedProgressBarInWebView.postValue(0)
                }
            }
            settings.setSupportZoom(true)
            settings.builtInZoomControls = true
        }
    }

    private fun letWebViewGoBackOnBackPressedButton() {
        requireActivity().onBackPressedDispatcher
            .addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    with(binding) {
                        if (webView.canGoBack()) {
                            webView.goBack()
                        } else if (isEnabled) {
                            isEnabled = false
                            requireActivity().onBackPressed()
                        }
                    }
                }
            })
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        binding.webView.saveState(outState)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        with(binding) {
            if (savedInstanceState != null) webView.restoreState(savedInstanceState)
            else webView.loadUrl(PokemonConstants.ABOUT_API)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}