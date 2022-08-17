package com.example.healthgates.ui.fragments

import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.healthgates.R
import com.example.healthgates.databinding.FragmentSignupComplimenteryBinding
import com.example.healthgates.ui.base.BaseFragment
import com.example.healthgates.ui.viewmodel.LoginViewModel
import com.example.healthgates.utils.navigateUsingPopUp

class SignUpComplimenteryFragment :
    BaseFragment<FragmentSignupComplimenteryBinding, LoginViewModel>() {
    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    )= FragmentSignupComplimenteryBinding.inflate(inflater, container, false)

    override fun getViewModel() = LoginViewModel::class.java

    override fun setupTheme() {

    }

    override fun setupClickListeners() {

        binding.tvOkBtn.setOnClickListener { navController.navigateUsingPopUp(R.id.signUpComplimenteryFragment, R.id.homeFragment) }

    }
}