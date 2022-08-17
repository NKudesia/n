package com.example.healthgates.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.example.healthgates.data.preferences.UserPreferences
import com.example.healthgates.singleton.dataStore

abstract class BaseFragment<viewBinding : ViewBinding, viewModel : ViewModel> : androidx.fragment.app.Fragment() {

    private var _binding: viewBinding? = null
    protected val binding get() = _binding!!
    protected lateinit var viewModel: viewModel
    protected lateinit var navController: NavController
    protected lateinit var userPreferences: UserPreferences

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        userPreferences = UserPreferences(requireContext().dataStore)
        _binding = getFragmentBinding(inflater, container)
        val factory = ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
        viewModel = ViewModelProvider(requireActivity(), factory).get(getViewModel())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = findNavController()
        setupTheme()
        setupClickListeners()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    abstract fun getFragmentBinding(inflater: LayoutInflater, container: ViewGroup?): viewBinding

    abstract fun getViewModel(): Class<viewModel>

    abstract fun setupTheme()

    abstract fun setupClickListeners()

}