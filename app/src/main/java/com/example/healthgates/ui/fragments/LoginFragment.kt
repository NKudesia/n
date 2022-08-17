package com.example.healthgates.ui.fragments

import android.app.Activity
import android.app.Dialog
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupWindow
import android.widget.TextView
import androidx.core.content.ContextCompat.getSystemService
import androidx.lifecycle.lifecycleScope
import com.android.volley.error.VolleyError
import com.bumptech.glide.load.resource.bitmap.CenterInside
import com.example.healthgates.R
import com.example.healthgates.data.models.User
import com.example.healthgates.databinding.DialogLoadingProgressBinding
import com.example.healthgates.databinding.FragmentLoginBinding
import com.example.healthgates.ui.activity.WebViewActivity
import com.example.healthgates.ui.base.BaseFragment
import com.example.healthgates.ui.interfaces.ApiListener
import com.example.healthgates.ui.viewmodel.LoginViewModel
import com.example.healthgates.utils.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.NonDisposableHandle.parent
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject

class LoginFragment : BaseFragment<FragmentLoginBinding, LoginViewModel>(), ApiListener {

    private lateinit var loadingDialog: Dialog
    private var locale = "ar"
    private var popupWindow = PopupWindow()

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentLoginBinding.inflate(inflater, container, false)

    override fun getViewModel() = LoginViewModel::class.java

    override fun setupTheme() {

        lifecycleScope.launch(Dispatchers.Main) {
            if (userPreferences.locale.first() != null) {
                locale = userPreferences.locale.first()!!
            }
        }
        setupDialog()
    }

    private fun setupDialog() {
        val loadingDialogBinding = DialogLoadingProgressBinding.inflate(layoutInflater, binding.root, false)
        loadingDialog = createDialog(loadingDialogBinding, R.drawable.progress_circle, false)
    }

    override fun setupClickListeners() {

        binding.tvForgotPass.setOnClickListener { navController.navigate(R.id.action_loginFragment_to_resetLinkFragment) }
        binding.signInBtn.setOnClickListener {
            checkEmailAndPassword()
        }
        binding.signUpBtn.setOnClickListener { navController.navigate(R.id.action_loginFragment_to_signUpFragment) }

        binding.tvLanguage.setOnClickListener{
            if (locale == "ar") {
                    updateAppLocale("en")
                }else {
                updateAppLocale("ar")
            }
            }

        binding.tvTerms.setOnClickListener{

//        val url = Intent(Intent.ACTION_VIEW)
//            url.data = Uri.parse("https://healthgates.pulseiehr.com/terms?#")
//            startActivity(url)

            val url = "https://healthgates.pulseiehr.com/terms"

            val intent = Intent(activity,WebViewActivity::class.java)
            intent.putExtra("url", url)
            startActivity(intent)
        }

        binding.tvPrivacyPolicy.setOnClickListener{

//            val url = Intent(Intent.ACTION_VIEW)
//            url.data = Uri.parse("https://healthgates.pulseiehr.com/privacy")
//            startActivity(url)

            val url = "https://healthgates.pulseiehr.com/privacy"

            val intent = Intent(activity,WebViewActivity::class.java)
            intent.putExtra("url", url)
            startActivity(intent)
        }

        binding.ivPhone.setOnClickListener{

            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:920011535")
            startActivity(intent)
            true
        }

        binding.ivFacebook.setOnClickListener{
            val url = "https://www.facebook.com/profile.php?id=100073646001020"

            val intent = Intent(activity,WebViewActivity::class.java)
            intent.putExtra("url", url)
            startActivity(intent)
        }

        binding.ivTwitter.setOnClickListener {
            val url = "https://twitter.com/healthgates_sa?s=21"

            val intent = Intent(activity,WebViewActivity::class.java)
            intent.putExtra("url", url)
            startActivity(intent)
        }

        binding.ivInstagram.setOnClickListener {
            val url = "https://instagram.com/healthgates_sa?utm_medium=copy_link"

            val intent = Intent(activity,WebViewActivity::class.java)
            intent.putExtra("url", url)
            startActivity(intent)
        }

    }


    private fun checkEmailAndPassword() {
        if (binding.etEmailMobile.editText?.text.toString().isEmpty()) {
            binding.etEmailMobile.showErrorOnEditText("* Required")
            return
        }
        binding.etEmailMobile.isErrorEnabled = false

        if (binding.etPassword.editText?.text.toString().isEmpty()) {
            binding.etPassword.showErrorOnEditText("* Required")
            return
        }
        if(!binding.cbTerms.isChecked){
            Popup(requireContext(),requireView(), resources.getString(R.string.please_accept_terms))
            return
        }
        binding.etPassword.isErrorEnabled = false
        login()
    }

    private fun login() {
        val user = User(login = binding.etEmailMobile.editText?.text.toString(), password = binding.etPassword.editText?.text.toString())
        loadingDialog.show()
        viewModel.login(user, this)
    }

    private fun finishActivity() {
        if (activity != null) {
            activity?.finish()
        }
    }


    private fun updateAppLocale(locale: String) {
        lifecycleScope.launch(Dispatchers.Main) {
            userPreferences.saveLocale(locale)
            LocaleUtil.applyLocalizedContext(requireActivity(), locale)
            requireActivity().recreate()
        }
    }

    override fun onSuccess(response: JSONObject) {
        Log.d(TAG, "onSuccess: $response")
        loadingDialog.dismiss()

        if (response.has("result")) {
            lifecycleScope.launch(Dispatchers.Main) {
//                    userPreferences.saveLoggedIn(true,
//                        response.getJSONObject("result").getInt("uid")
//                    )
                userPreferences.saveLoggedIn(true,
                    response.getJSONObject("result").getInt("partner_id")
                )
                navController.navigateUsingPopUp(R.id.loginFragment, R.id.action_global_homeFragment)
            }
        } else {
//            popupWindow = PopupWindow(resources.getString(R.string.account_doesnt_exist))
//            popupWindow?.isOutsideTouchable = true
//            popupWindow?.isFocusable = true
//            popupWindow.showAtLocation(view, Gravity.CENTER, 0 , 0)

            Popup(requireContext(),requireView(), resources.getString(R.string.account_doesnt_exist))
//            onFailure(VolleyError("Account doesn't exist! Maybe wrong email/mobile or password!"))
        }
    }

    override fun onFailure(error: VolleyError) {
        Log.d(TAG, "onFailure: ${error.message}")
        loadingDialog.dismiss()
//        popupWindow = PopupWindow("Something went wrong! ${error.message}")
//        popupWindow?.isOutsideTouchable = true
//        popupWindow?.isFocusable = true
//        popupWindow.showAtLocation(view, Gravity.CENTER, 0 , 0)

        Popup(requireContext(),requireView(), "onFailure: ${error.message}")
//        binding.root.snackbar("Something went wrong! ${error.message}")
    }

    private fun PopupWindow(message: String): PopupWindow {
        val inflater = activity?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.layout_popup, null)
        val tvMessage = view.findViewById<TextView>(R.id.tv_message)
        val tvOkBtn = view.findViewById<TextView>(R.id.tv_ok_btn);

        tvMessage.text = message

        tvOkBtn.setOnClickListener(){
         dismissPopup()
        }

        return PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    private  fun dismissPopup(){
        popupWindow.dismiss()
    }

}