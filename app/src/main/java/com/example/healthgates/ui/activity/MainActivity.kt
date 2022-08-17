package com.example.healthgates.ui.activity

import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.lifecycleScope
import androidx.navigation.ui.setupWithNavController
import com.example.healthgates.R
import com.example.healthgates.data.preferences.UserPreferences
import com.example.healthgates.databinding.ActivityMainBinding
import com.example.healthgates.singleton.dataStore
import com.example.healthgates.ui.base.BaseActivity
import com.example.healthgates.ui.fragments.BranchesFragment
import com.example.healthgates.ui.fragments.HomeFragment
import com.example.healthgates.ui.interfaces.AlertDialogCallback
import com.example.healthgates.ui.viewmodel.LoginViewModel
import com.example.healthgates.ui.viewmodel.PatientViewModel
import com.example.healthgates.utils.LocaleUtil
import com.example.healthgates.utils.navigateUsingPopUp
import com.example.healthgates.utils.showAlertDialog
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

private const val HOME_FRAGMENT = 1

class MainActivity : BaseActivity<ActivityMainBinding, PatientViewModel>(), AlertDialogCallback {

    private var currentFragment = -1
    protected var locale = "ar"

    override fun getActivityBinding(inflater: LayoutInflater)= ActivityMainBinding.inflate(inflater)

    override fun getViewModel()= PatientViewModel::class.java

    override fun setupTheme() {
//        updateAppLocale("ar")
        lifecycleScope.launch() {
            if(UserPreferences(dataStore).locale.first() != null) {
                locale = UserPreferences(dataStore).locale.first()!!
            }
                LocaleUtil.applyLocalizedContext(baseContext, locale)
        }
    }

    override fun setupNavigation() {
        binding.navView.setupWithNavController(navController)
        binding.navView.itemIconTintList = null

        navController.addOnDestinationChangedListener { _, destination, _ ->
            val id = destination.id

            if (
                id == R.id.homeFragment ||
                id == R.id.branchesFragment ||
                id == R.id.viewDoctorsFragment ||
            id == R.id.doctorFragment || id == R.id.bookAppointmentDateFragment ||
                    id == R.id.bookAppointmentTimeFragment ||
                    id == R.id.profileFragment ||
                    id == R.id.appointmentsFragment ||
                    id == R.id.appointmentDetailsFragment ||
                    id == R.id.prescriptionsFragment ||
                    id == R.id.labTestsFragment ||
                    id == R.id.radResultsFragment ||
                    id == R.id.mapFragment) {
                with(binding) {
                    navView.visibility = View.VISIBLE
                }
            }else with(binding) {
                    navView.visibility = View.GONE
            }

//            if(id == R.id.appointmentsFragment)with(binding){
//                btnAppointment.visibility = View.VISIBLE
//            }else with(binding){
//                btnAppointment.visibility = View.GONE
//            }

        }

        binding.navView.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.homeFragment -> {
                    navController.navigate(R.id.homeFragment)
                    true
                }
                R.id.branchesFragment -> {
                    navController.navigate(R.id.branchesFragment)
                    true
                }
                R.id.call -> {
                    val intent = Intent(Intent.ACTION_DIAL)
                    intent.data = Uri.parse("tel:920011535")
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }

        binding.navView.setOnNavigationItemReselectedListener {
            when (it.itemId) {
                R.id.homeFragment -> {
                  if(it.itemId != navController.currentDestination?.id){
                        navController.navigate(R.id.homeFragment )
                    }
                    true
                }
                R.id.branchesFragment -> {
                    if(it.itemId != navController.currentDestination?.id){
                        navController.navigate(R.id.branchesFragment)
                    }
                    true
                }
                R.id.call -> {
                    val intent = Intent(Intent.ACTION_DIAL)
                    intent.data = Uri.parse("tel:920011535")
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }

//        binding.btnAppointment.setOnClickListener(){
//            if(navController.currentDestination!!.id != R.id.viewDoctorsFragment) {
//                viewModel.setDoctorListMode(1)
//                navController.navigate(R.id.viewDoctorsFragment)
//            }
//        }
    }

    private fun updateAppLocale(locale: String) {
        LocaleUtil.applyLocalizedContext(applicationContext, locale)
    }


    override fun onBackPressed() {
        when (currentFragment) {
            HOME_FRAGMENT -> {
                showAlertDialog(this, title = getString(R.string.exit_app), subTitle = getString(R.string.exit_app_body), listener = this)
                return
            }
        }
        super.onBackPressed()
    }

    override fun onNegativeButtonClick(dialog: DialogInterface) {
        dialog.dismiss()
    }

    override fun onPositiveButtonClick() {
        if (currentFragment == HOME_FRAGMENT) {
            finishAndRemoveTask()
        }
    }
}