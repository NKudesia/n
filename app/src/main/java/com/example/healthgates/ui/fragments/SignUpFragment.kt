package com.example.healthgates.ui.fragments

import android.app.Dialog
import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemClickListener
import android.widget.ArrayAdapter
import android.widget.RadioButton
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.android.volley.*
import com.android.volley.error.AuthFailureError
import com.android.volley.error.VolleyError
import com.android.volley.request.StringRequest
import com.android.volley.toolbox.Volley
import com.example.healthgates.R
import com.example.healthgates.data.config.AppConfig
import com.example.healthgates.data.models.User
import com.example.healthgates.databinding.DialogLoadingProgressBinding
import com.example.healthgates.databinding.FragmentSignupBinding
import com.example.healthgates.singleton.MyApplication
import com.example.healthgates.singleton.VolleySingleton
import com.example.healthgates.ui.base.BaseFragment
import com.example.healthgates.ui.interfaces.ApiListener
import com.example.healthgates.ui.viewmodel.LoginViewModel
import com.example.healthgates.utils.*
import com.google.android.material.radiobutton.MaterialRadioButton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
//import org.webrtc.ContextUtils.getApplicationContext


class SignUpFragment : BaseFragment<FragmentSignupBinding, LoginViewModel>(), ApiListener {

    private lateinit var loadingDialog: Dialog
    var countryArrayList = ArrayList<String>()
    private lateinit var countryResult: JSONArray
    lateinit var radioButton: MaterialRadioButton
    var country_id = ""
    var gender = ""
    var selection: String? = null


    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) =  FragmentSignupBinding.inflate(inflater, container, false)

    override fun getViewModel() = LoginViewModel::class.java

    override fun setupTheme() {

        setupDialog()
    }

    private fun setupDialog() {
        val loadingDialogBinding = DialogLoadingProgressBinding.inflate(layoutInflater, binding.root, false)
        loadingDialog = createDialog(loadingDialogBinding, R.drawable.progress_circle, false)
    }



    override fun setupClickListeners() {

//        binding.acNationality.setOnItemClickListener { parent, view, position, id ->
//            getCountry()
//            binding.acNationality.showDropDown()
//            getCountryId(position)
//            selection = parent.getItemAtPosition(position) as String
//            binding.acNationality.setText("    " + parent.getItemAtPosition(position))
//            Toast.makeText(
//                getApplicationContext(), selection,
//                Toast.LENGTH_SHORT
//            )
//        }

        binding.acNationality.setOnClickListener{
            getCountry()
            binding.acNationality.showDropDown()
        }

        binding.acNationality.onItemClickListener = AdapterView.OnItemClickListener{
                parent, view, position, id ->
            getCountry()
            binding.acNationality.showDropDown()
            getCountryId(position)
            selection = parent.getItemAtPosition(position) as String
            binding.acNationality.setText("    " + parent.getItemAtPosition(position))
            Log.d(TAG, "setupClickListeners: "+ getCountryId(position))
        }

        binding.submitBtn.setOnClickListener { checkEmailandPassword()}

        binding.tvTerms.setOnClickListener{

            val url = Intent(Intent.ACTION_VIEW)
            url.data = Uri.parse("https://healthgates.pulseiehr.com/terms?#")
            startActivity(url)
        }

        binding.tvPrivacyPolicy.setOnClickListener{

            val url = Intent(Intent.ACTION_VIEW)
            url.data = Uri.parse("https://healthgates.pulseiehr.com/privacy")
            startActivity(url)
        }

    }

    private fun checkEmailandPassword(){
        if (binding.etName.editText?.text.toString().isEmpty()) {
            binding.etName.showErrorOnEditText("* Required")
            return
        }
        binding.etName.isErrorEnabled = false

        if (binding.etMobileNum.editText?.text.toString().isEmpty()) {
            binding.etMobileNum.showErrorOnEditText("* Required")
            return
        }
        binding.etMobileNum.isErrorEnabled = false

        if (binding.etEmail.editText?.text.toString().isEmpty()) {
            binding.etEmail.showErrorOnEditText("* Required")
            return
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(binding.etEmail.editText?.text.toString()).matches()) {
            binding.etEmail.showErrorOnEditText(getString(R.string.invalid_email))
            return
        }
        binding.etEmail.isErrorEnabled = false

        if (binding.etPassword.editText?.text.toString().isEmpty()) {
            binding.etPassword.showErrorOnEditText("* Required")
            return
        }
        binding.etPassword.isErrorEnabled = false

        if (selection.toString().isEmpty()){
            binding.acNationality.background = ContextCompat.getDrawable(requireContext(), R.drawable.ic_texfield_error_bg)
            return
        }
        binding.acNationality.background = ContextCompat.getDrawable(requireContext(), R.drawable.ic_textfield_bg)

        if(!binding.cbTerms.isChecked){
            Popup(requireContext(),requireView(), resources.getString(R.string.please_accept_terms))
            return
        }

        if (binding.radioBtnMale.isChecked){
            gender = "male"
        }else if (binding.radioBtnFemale.isChecked){
            gender = "female"
        }


        signUp()
    }

    private fun signUp(){
        val user = User(
            login = binding.etEmail.editText?.text.toString(),
            name = binding.etName.editText?.text.toString(),
            mobile = binding.etMobileNum.editText?.text.toString(),
            nationality = country_id,
            gender = gender,
            password = binding.etPassword.editText?.text.toString())
        Log.d(TAG, "signUp: "+ country_id + gender)
        loadingDialog.show()
        viewModel.signUp(user, this)
    }


    private fun getCountry() {

        val paramsObject = JSONObject()
        with(paramsObject) {
        }
        val jsonObject = JSONObject()
        with(jsonObject){
            put("jsonrpc", AppConfig.JSON_RPC)
            put("method", AppConfig.METHOD)
            put("params", paramsObject)
            put("id", "505136376")
        }


        val stringRequest = object : StringRequest(
            Request.Method.POST, "https://healthgates.pulseiehr.com/api/nationality",
            Response.Listener<String?> { response ->
                var j: JSONObject?
                try {
                    j = JSONObject(response)

                    countryResult = j.getJSONArray("result")
                    //Fetch Country List
                    countryDetails(countryResult)

                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            },
            Response.ErrorListener { error ->
                Toast.makeText(
                    requireContext(),
                    "" + error,
                    Toast.LENGTH_LONG
                ).show()
            })
        {

            @Throws(AuthFailureError::class)
            override fun getBody(): ByteArray {
                return jsonObject.toString().toByteArray()
            }

            override fun getBodyContentType(): String {
                return "application/json"
            }
//            override fun getHeaders(): MutableMap<String, String> {
//                val params: MutableMap<String, String> = HashMap()
//                params["Accept"] = "application/json"
//                return params
//            }
        }
        val requestQueue = Volley.newRequestQueue(requireContext())
        val socketTimeout = 30000
        val policy: RetryPolicy = DefaultRetryPolicy(
            socketTimeout,
            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        stringRequest.retryPolicy = policy
//        requestQueue.add(stringRequest)
        VolleySingleton.getInstance(MyApplication.getAppContext()).addToRequestQueue(stringRequest)
    }

    private fun countryDetails(result: JSONArray) {
        countryArrayList = ArrayList()
        for (i in 0..result!!.length() - 1) {
            try {
                val json = result.getJSONObject(i)
                countryArrayList.add(json.getString("name"))
                Log.d(TAG, "countryDetails: "+ countryArrayList)
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }
        binding.acNationality.setAdapter<ArrayAdapter<String>>(
            ArrayAdapter(
                requireContext(),
                android.R.layout.simple_spinner_dropdown_item,
                countryArrayList
            )
        )
    }

    private fun getCountryId(position: Int) {
        try {
            //Getting object of given index
            val json = countryResult!!.getJSONObject(position)
            //Fetching name from that object
            country_id = json.getString("id")

        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }

    override fun onSuccess(response: JSONObject) {
        Log.d(ContentValues.TAG, "onSuccess: $response")
        loadingDialog.dismiss()

        if (response.has("result")) {
            val result = response.getJSONObject("result")
            if (result.getBoolean("success")) {
                lifecycleScope.launch(Dispatchers.Main) {
//                    userPreferences.saveLoggedIn(true,
//                        response.getJSONObject("result").getInt("uid")
//                    )
                    userPreferences.saveLoggedIn(true,
                        response.getJSONObject("result").getInt("partner_id")
                    )
//                    userPreferences.savePatientId(result.getInt("patient_id"))
                    navController.navigateUsingPopUp(R.id.signUpFragment , R.id.action_signUpFragment_to_signUpComplimentryFragment)
                }
            }  else {

                onFailure(VolleyError(result.getString("msg")))
            }
            return
        }
        onFailure(VolleyError("Try again!"))
    }

    override fun onFailure(error: VolleyError) {
        Log.d(TAG, "onFailure: "+ error)
        loadingDialog.dismiss()
//        binding.root.snackbar("Something went wrong! ${error.message}")
        Popup(requireContext(),requireView(), "Something Went Wrong! ${error.message}")
//        navController.navigate(R.id.action_signUpFragment_to_signUpFailedFragment)
    }

}