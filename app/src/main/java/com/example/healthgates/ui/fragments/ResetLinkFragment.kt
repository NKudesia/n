package com.example.healthgates.ui.fragments

import android.app.Dialog
import android.content.ContentValues
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.android.volley.error.VolleyError
import com.example.healthgates.R
import com.example.healthgates.data.models.User
import com.example.healthgates.databinding.DialogLoadingProgressBinding
import com.example.healthgates.databinding.FragmentResetLinkBinding
import com.example.healthgates.ui.base.BaseFragment
import com.example.healthgates.ui.interfaces.ApiListener
import com.example.healthgates.ui.viewmodel.LoginViewModel
import com.example.healthgates.utils.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject

class ResetLinkFragment : BaseFragment<FragmentResetLinkBinding, LoginViewModel>(), ApiListener {

    private lateinit var loadingDialog: Dialog

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    )= FragmentResetLinkBinding.inflate(inflater, container, false)
    override fun getViewModel()= LoginViewModel::class.java

    override fun setupTheme() {
        setupDialog()
    }

    private fun setupDialog() {
        val loadingDialogBinding = DialogLoadingProgressBinding.inflate(layoutInflater, binding.root, false)
        loadingDialog = createDialog(loadingDialogBinding, R.drawable.progress_circle, false)
    }

    override fun setupClickListeners() {
        binding.sendBtn.setOnClickListener { checkEmail() }
    }

    private fun checkEmail(){
        if (binding.etEmail.editText?.text.toString().isEmpty()) {
            binding.etEmail.showErrorOnEditText("* Required")
            return
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(binding.etEmail.editText?.text.toString()).matches()) {
            binding.etEmail.showErrorOnEditText(getString(R.string.invalid_email))
            return
        }
        binding.etEmail.isErrorEnabled = false
        resetPassword()

//        if (binding.etMobileNum.editText?.text.toString().isEmpty()) {
//            binding.etMobileNum.showErrorOnEditText("* Required")
//            return
//        }
//        binding.etMobileNum.isErrorEnabled = false

    }

    private fun resetPassword(){
        val email : String = binding.etEmail.editText?.text.toString()
        loadingDialog.show()
        viewModel.resetPassword(email, this)
    }

    override fun onSuccess(response: JSONObject) {
        Log.d(ContentValues.TAG, "onSuccess: $response")
        loadingDialog.dismiss()

        if (response.has("result")) {
            val result = response.getJSONObject("result")
            if (result.getBoolean("success")) {
                lifecycleScope.launch(Dispatchers.Main) {
                    navController.navigate(R.id.action_resetLinkFragment_to_resetComplimenteryFragment)
                }
            }  else {
                onFailure(VolleyError(result.getString("msg")))
            }

        } else {
            onFailure(VolleyError("Account doesn't exist! Maybe wrong email/mobile or password!"))
        }
    }

    override fun onFailure(error: VolleyError) {

        Log.d(ContentValues.TAG, "onFailure: ${error.message}")
        loadingDialog.dismiss()
//        binding.root.snackbar("Something went wrong! ${error.message}")
        Popup(requireContext(),requireView(), "Something Went Wrong! ${error.message}")

    }

}





//package com.example.healthgates.ui.fragments
//
//import android.app.Dialog
//import android.content.ContentValues
//import android.util.Log
//import android.util.Patterns
//import android.view.LayoutInflater
//import android.view.ViewGroup
//import androidx.lifecycle.lifecycleScope
//import com.android.volley.error.VolleyError
//import com.example.healthgates.R
//import com.example.healthgates.data.models.User
//import com.example.healthgates.databinding.DialogLoadingProgressBinding
//import com.example.healthgates.databinding.FragmentResetLinkBinding
//import com.example.healthgates.ui.base.BaseFragment
//import com.example.healthgates.ui.interfaces.ApiListener
//import com.example.healthgates.ui.viewmodel.LoginViewModel
//import com.example.healthgates.utils.Popup
//import com.example.healthgates.utils.createDialog
//import com.example.healthgates.utils.showErrorOnEditText
//import com.example.healthgates.utils.snackbar
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.launch
//import org.json.JSONObject
//
//class ResetLinkFragment : BaseFragment<FragmentResetLinkBinding, LoginViewModel>(), ApiListener {
//
//    private lateinit var loadingDialog: Dialog
//
//    override fun getFragmentBinding(
//        inflater: LayoutInflater,
//        container: ViewGroup?
//    )= FragmentResetLinkBinding.inflate(inflater, container, false)
//    override fun getViewModel()= LoginViewModel::class.java
//
//    override fun setupTheme() {
//        setupDialog()
//    }
//
//    private fun setupDialog() {
//        val loadingDialogBinding = DialogLoadingProgressBinding.inflate(layoutInflater, binding.root, false)
//        loadingDialog = createDialog(loadingDialogBinding, R.drawable.progress_circle, false)
//    }
//
//    override fun setupClickListeners() {
//        binding.sendBtn.setOnClickListener { checkEmail() }
//    }
//
//    private fun checkEmail(){
//        if (binding.etEmail.editText?.text.toString().isEmpty()) {
//            binding.etEmail.showErrorOnEditText("* Required")
//            return
//        }
//        if (!Patterns.EMAIL_ADDRESS.matcher(binding.etEmail.editText?.text.toString()).matches()) {
//            binding.etEmail.showErrorOnEditText(getString(R.string.invalid_email))
//            return
//        }
//        binding.etEmail.isErrorEnabled = false
//        resetPassword()
//
////        if (binding.etMobileNum.editText?.text.toString().isEmpty()) {
////            binding.etMobileNum.showErrorOnEditText("* Required")
////            return
////        }
////        binding.etMobileNum.isErrorEnabled = false
//
//    }
//
//    private fun resetPassword(){
//        val email : String = binding.etEmail.editText?.text.toString()
//        loadingDialog.show()
//        viewModel.resetPassword(email, this)
//    }
//
//    override fun onSuccess(response: JSONObject) {
//        Log.d(ContentValues.TAG, "onSuccess: $response")
//        loadingDialog.dismiss()
//
//        if (response.has("result")) {
//            lifecycleScope.launch(Dispatchers.Main) {
//                navController.navigate(R.id.action_resetLinkFragment_to_resetComplimenteryFragment)
//            }
//        } else {
//            onFailure(VolleyError("Account doesn't exist! Maybe wrong email/mobile or password!"))
//        }
//    }
//
//    override fun onFailure(error: VolleyError) {
//
//        Log.d(ContentValues.TAG, "onFailure: ${error.message}")
//        loadingDialog.dismiss()
////        binding.root.snackbar("Something went wrong! ${error.message}")
//        Popup(requireContext(),requireView(), "Something Went Wrong! ${error.message}")
//
//    }
//
//}