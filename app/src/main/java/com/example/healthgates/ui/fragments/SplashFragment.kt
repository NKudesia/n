
package com.example.healthgates.ui.fragments

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.example.healthgates.R
import com.example.healthgates.databinding.FragmentSplashBinding
import com.example.healthgates.ui.base.BaseFragment
import com.example.healthgates.ui.viewmodel.LoginViewModel
import com.example.healthgates.utils.navigateUsingPopUp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SplashFragment : BaseFragment<FragmentSplashBinding, LoginViewModel>() {

    override fun getFragmentBinding(inflater: LayoutInflater, container: ViewGroup?) = FragmentSplashBinding.inflate(inflater, container, false)

    override fun getViewModel() = LoginViewModel::class.java

    override fun onResume() {
        super.onResume()
        lifecycleScope.launch(Dispatchers.IO) {
            val loggedIn = userPreferences.loggedIn.first()
            delay(1500)
            withContext(Dispatchers.Main) {
                if (loggedIn == true) {
                    navController.navigateUsingPopUp(R.id.splashFragment, R.id.action_global_homeFragment)
                } else {
                    navController.navigateUsingPopUp(R.id.splashFragment, R.id.action_splashFragment_to_loginFragment)
                }
            }
        }
    }

    override fun setupTheme() {}

    override fun setupClickListeners() {}

}