package com.example.healthgates.ui.base

import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.viewbinding.ViewBinding
import com.example.healthgates.R
import com.example.healthgates.data.preferences.UserPreferences
import com.example.healthgates.singleton.dataStore
import com.example.healthgates.utils.LocaleUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

abstract class BaseActivity<viewBinding : ViewBinding, viewModel : ViewModel> : AppCompatActivity() {

    protected lateinit var binding: viewBinding
    protected lateinit var viewModel: viewModel
    protected lateinit var navController: NavController
//    protected lateinit var userPreferences: UserPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        userPreferences = UserPreferences(dataStore)
        binding = getActivityBinding(layoutInflater)
        setContentView(binding.root)

        val factory = ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        viewModel = ViewModelProvider(this, factory).get(getViewModel())

        navController = findNavController(R.id.nav_host_fragment)
        setupTheme()
        setupNavigation()
        resetTitle()
    }

    abstract fun getActivityBinding(inflater: LayoutInflater): viewBinding

    abstract fun getViewModel(): Class<viewModel>

    abstract fun setupTheme()

    abstract fun setupNavigation()

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun resetTitle() {
        try {
            val label = packageManager.getActivityInfo(componentName, PackageManager.GET_META_DATA).labelRes;
            if (label != 0) {
                setTitle(label);
            }
        } catch (e: PackageManager.NameNotFoundException) {}
    }

//    override fun attachBaseContext(newBase: Context) {
////        lifecycleScope.launch() {
////            if(UserPreferences(newBase.dataStore).locale.first() != null) {
////                locale = UserPreferences(newBase.dataStore).locale.first()!!
////            }
////        }
//            applyOverrideConfiguration(LocaleUtil.getLocalizedConfiguration(locale))
//        super.attachBaseContext(newBase)
//    }

}