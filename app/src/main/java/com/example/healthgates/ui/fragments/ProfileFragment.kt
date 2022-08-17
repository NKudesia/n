package com.example.healthgates.ui.fragments

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.lifecycle.lifecycleScope
import com.example.healthgates.R
import com.example.healthgates.data.models.ProfileItem
import com.example.healthgates.data.models.UserDetails
import com.example.healthgates.databinding.FragmentProfileBinding
import com.example.healthgates.ui.activity.WebViewActivity
import com.example.healthgates.ui.adapter.ProfileScreenAdapter
import com.example.healthgates.ui.base.BaseFragment
import com.example.healthgates.ui.viewmodel.PatientViewModel
import com.example.healthgates.utils.LocaleUtil
import com.example.healthgates.utils.loadImage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.math.RoundingMode
import java.text.DecimalFormat

class ProfileFragment : BaseFragment<FragmentProfileBinding, PatientViewModel>() {

    private lateinit var profileScreenAdapter: ProfileScreenAdapter

    private var locale = "ar"

    private var isChecked = false

    override fun getFragmentBinding(inflater: LayoutInflater, container: ViewGroup?) = FragmentProfileBinding.inflate(inflater, container, false)

    override fun getViewModel() = PatientViewModel::class.java

//    @SuppressLint("ClickableViewAccessibility")
    override fun setupTheme() {
        binding.bmiSeekBar.setOnTouchListener { _, _ ->
            return@setOnTouchListener true
        }
        initRecyclerView()

    lifecycleScope.launch(Dispatchers.Main) {
        if(userPreferences.locale.first() != null){
            locale = userPreferences.locale.first()!!
        }
        viewModel.getCurrentUser(userPreferences.userId.first()!!).observe(viewLifecycleOwner, {
            it?.let { userDetails ->
                updateUI(userDetails)
            }
        })
    }
    }

    override fun setupClickListeners() {

        binding.layout2.setOnClickListener() {

            lifecycleScope.launch(Dispatchers.Main) {
                userPreferences.clear()
                navController.navigate(R.id.action_profileragment_to_loginFragment)
            }
        }

        binding.radioGroup.setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener { group, checkedId ->
            if (checkedId == R.id.rb_arabic) {
                if(isChecked) {
                    updateAppLocale("ar")
                }
            }else{
                if(isChecked) {
                    updateAppLocale("en")
                }
            }
        })

        binding.tvTerms.setOnClickListener{

//        val url = Intent(Intent.ACTION_VIEW)
//            url.data = Uri.parse("https://healthgates.pulseiehr.com/terms?#")
//            startActivity(url)

            val url = "https://healthgates.pulseiehr.com/terms"

            val intent = Intent(activity, WebViewActivity::class.java)
            intent.putExtra("url", url)
            startActivity(intent)
        }

        binding.tvPrivacyPolicy.setOnClickListener{

//            val url = Intent(Intent.ACTION_VIEW)
//            url.data = Uri.parse("https://healthgates.pulseiehr.com/privacy")
//            startActivity(url)
//
            val url = "https://healthgates.pulseiehr.com/privacy"

            val intent = Intent(activity, WebViewActivity::class.java)
            intent.putExtra("url", url)
            startActivity(intent)
        }

    }

    private fun initRecyclerView() {
        profileScreenAdapter = ProfileScreenAdapter()
        binding.recyclerView.adapter = profileScreenAdapter
        updateProfileScreenList()

    }

    private fun updateUI(userDetails: UserDetails) {
        binding.ivProfile.loadImage(userDetails.image_1920)
        binding.tvUserName.text = userDetails.name
        binding.tvAge.text = "${userDetails.age}, ${userDetails.gender}"

        val df = DecimalFormat("#.##")
        df.roundingMode = RoundingMode.CEILING
        binding.tvFile.text = String.format(getString(R.string.file), userDetails.code)
        binding.tvId.text = String.format(getString(R.string.id), userDetails.id)

        if (userDetails.insurance.toBoolean()){
            binding.tvInsurance.text = String.format(getString(R.string.insurance), "Active")
        }else{
            binding.tvInsurance.text = String.format(getString(R.string.insurance), "Inactive")
        }

        if (locale == "ar") {
            binding.rbArabic.isChecked = true
        } else {
            binding.rbEnglish.isChecked = true
        }

        isChecked = true

        binding.tvMobile.text = String.format(getString(R.string.mobile_two), userDetails.mobile)
        binding.tvEmail.text = String.format(getString(R.string.email_two), userDetails.email)

        binding.tvInsuranceTwo.text = String.format(getString(R.string.insurance_two), userDetails.insurance)
        binding.tvClass.text = String.format(getString(R.string.class_two), userDetails.class_name)
        binding.tvPolicyNo.text = String.format(getString(R.string.policy_no), userDetails.policy_number)
        binding.tvExpiry.text = String.format(getString(R.string.expiry), userDetails.validity)

        binding.tvBmi.text = String.format(getString(R.string.your_bmi), df.format(userDetails.bmi).toString())
        binding.bmiSeekBar.progress = userDetails.bmi.toInt()
        updateProfileScreenList(userDetails)
    }

    private fun updateProfileScreenList(userDetails: UserDetails) {
        val list: ArrayList<ProfileItem> = ArrayList()
        with(list) {
            add(ProfileItem(sideDividerVisible = true, value = userDetails.height.toString(), type = getString(R.string.cm_height)))
            add(ProfileItem(sideDividerVisible = true, value = userDetails.weight.toString(), type = getString(R.string.kg_weight)))
            add(ProfileItem(sideDividerVisible = false, value = userDetails.blood_group.toString(), type = getString(R.string.blood_type)))
        }
        profileScreenAdapter.updateList(list)
    }

    private fun updateProfileScreenList() {
        val list: ArrayList<ProfileItem> = ArrayList()
        with(list) {
            add(ProfileItem(sideDividerVisible = true, value = "179", type = getString(R.string.cm_height)))
            add(ProfileItem(sideDividerVisible = true, value = "95", type = getString(R.string.kg_weight)))
            add(ProfileItem(sideDividerVisible = false, value = "AB+", type = getString(R.string.blood_type)))
        }
        profileScreenAdapter.updateList(list)
    }

    private fun updateAppLocale(locale: String) {
        lifecycleScope.launch(Dispatchers.Main) {
            userPreferences.saveLocale(locale)
            LocaleUtil.applyLocalizedContext(requireActivity(), locale)
            requireActivity().recreate()
        }
    }


}