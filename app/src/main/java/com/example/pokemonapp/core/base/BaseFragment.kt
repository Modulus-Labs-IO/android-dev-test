package com.example.pokemonapp.core.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.example.pokemonapp.core.base.hilt.HiltFragmentEntryPoint

abstract class BaseFragment <VB : ViewBinding>(
    private val setupViewBinding: (LayoutInflater) -> VB
) : HiltFragmentEntryPoint() {

    lateinit var binding: VB

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = setupViewBinding(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        observeState()
        setupListeners()
    }

    open fun setupView() {}

    open fun setupListeners() {}

    open fun observeState() {}

}