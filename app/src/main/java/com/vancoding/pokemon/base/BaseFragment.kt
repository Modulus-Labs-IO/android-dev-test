package com.vancoding.pokemon.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.vancoding.pokemon.R

abstract class BaseFragment<VB : ViewBinding> : Fragment() {

    private var _binding: VB? = null
    protected val binding get() = _binding!!

    abstract fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?): VB

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = getViewBinding(inflater, container)
        return binding.root
    }

    protected fun showToast(message: String, duration: Int = Toast.LENGTH_SHORT) {
        Toast.makeText(requireContext(), message, duration).show()
    }

    protected fun showError(message: String, errorView: View? = null) {
        if (errorView != null) {
            errorView.visibility = View.VISIBLE
            if (errorView is android.widget.TextView) {
                errorView.text = getString(R.string.error_message, message)
            }
        } else {
            showToast(getString(R.string.error_message, message))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}