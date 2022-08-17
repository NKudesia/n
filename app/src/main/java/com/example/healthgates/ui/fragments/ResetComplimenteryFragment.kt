package com.example.healthgates.ui.fragments

import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.healthgates.R
import com.example.healthgates.databinding.FragmentResetComplimenteryBinding
import com.example.healthgates.ui.base.BaseFragment
import com.example.healthgates.ui.viewmodel.LoginViewModel
import com.example.healthgates.utils.navigateUsingPopUp

class ResetComplimenteryFragment : BaseFragment<FragmentResetComplimenteryBinding, LoginViewModel>() {
    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    )= FragmentResetComplimenteryBinding.inflate(inflater, container, false)

    override fun getViewModel()= LoginViewModel::class.java

    override fun setupTheme() {

    }

    override fun setupClickListeners() {

        binding.okBtn.setOnClickListener { navController.navigateUsingPopUp(R.id.resetComplimenteryFragment, R.id.loginFragment) }

    }
}



//package com.example.healthgates.ui.fragments
//
//import android.view.LayoutInflater
//import android.view.ViewGroup
//import com.example.healthgates.R
//import com.example.healthgates.databinding.FragmentResetComplimenteryBinding
//import com.example.healthgates.ui.base.BaseFragment
//import com.example.healthgates.ui.viewmodel.LoginViewModel
//import com.example.healthgates.utils.navigateUsingPopUp
//
//class ResetComplimenteryFragment : BaseFragment<FragmentResetComplimenteryBinding, LoginViewModel>() {
//    override fun getFragmentBinding(
//        inflater: LayoutInflater,
//        container: ViewGroup?
//    )= FragmentResetComplimenteryBinding.inflate(inflater, container, false)
//
//    override fun getViewModel()= LoginViewModel::class.java
//
//    override fun setupTheme() {
//
//    }
//
//    override fun setupClickListeners() {
//
//        binding.okBtn.setOnClickListener { navController.navigateUsingPopUp(R.id.resetComplimenteryFragment, R.id.action_global_homeFragment) }
//
//    }
//}