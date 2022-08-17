package com.example.healthgates.ui.fragments

import android.Manifest
import android.app.Dialog
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.android.volley.error.VolleyError
import com.example.healthgates.R
import com.example.healthgates.data.models.Doctor
import com.example.healthgates.databinding.DialogLoadingProgressBinding
import com.example.healthgates.databinding.FragmentDoctorBinding
import com.example.healthgates.ui.base.BaseFragment
import com.example.healthgates.ui.interfaces.ApiListener
import com.example.healthgates.ui.interfaces.ChannelIdApiListener
import com.example.healthgates.ui.viewmodel.LoginViewModel
import com.example.healthgates.ui.viewmodel.PatientViewModel
import com.example.healthgates.utils.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.json.JSONObject

class DoctorFragment : BaseFragment<FragmentDoctorBinding, PatientViewModel>(), ApiListener {

    private var userId: Int = 2
    private lateinit var selectedDoctor: Doctor
    private lateinit var loadingDialog: Dialog

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    )= FragmentDoctorBinding.inflate(inflater, container, false)
    override fun getViewModel()= PatientViewModel::class.java

    override fun setupTheme() {
        selectedDoctor = viewModel.getSelectedDoctor()!!
        lifecycleScope.launch(Dispatchers.Main) {
            userId = userPreferences.patientId.first()!!
            viewModel.readDoctorDetails(userId, selectedDoctor.id, this@DoctorFragment)
        }
        updateUI(selectedDoctor)
        setupDialog()
    }

    private fun setupDialog() {
        val loadingDialogBinding = DialogLoadingProgressBinding.inflate(layoutInflater, binding.root, false)
        loadingDialog = createDialog(loadingDialogBinding, R.drawable.progress_circle, false)
    }

    override fun setupClickListeners() {

        binding.bookAppointment.setOnClickListener(){
            viewModel.setBookedOnline(false)
            navController.navigate(R.id.action_doctorFragment_to_bookAppointmentDateFragment)
        }

//        binding.videoCall.setOnClickListener(){
////            callDoctor()
//            viewModel.setBookedOnline(true)
//            navController.navigate(R.id.action_doctorFragment_to_bookAppointmentDateFragment)
//        }
    }


    private fun updateUI(doctor: Doctor) {
        with(binding) {
            tvName.text = doctor.name
            tvSpeciality.text = String.format(root.context.getString(R.string.specialist), selectedDoctor.speciality.name)
            tvEducation.text = doctor.description
            ivProfile.loadImage(selectedDoctor.profileImg)
        }
    }

    private fun checkAllPermissions(): Boolean {
        if (!requireContext().hasPermissions(Manifest.permission.RECORD_AUDIO)) {
            return false
        }
        return requireContext().hasPermissions(Manifest.permission.CAMERA)
    }

    private fun callDoctor() {
        if (!checkAllPermissions()) {
            requestPermissions(arrayOf(Manifest.permission.RECORD_AUDIO, Manifest.permission.CAMERA), 101)
            return
        }
//        if (!selectedDoctor.isOnline) {
//            binding.root.snackbar("The doctor is currently not available!")
//            return
//        }
        loadingDialog.show()

        viewModel.generateTokenAndChannelId(selectedDoctor.user_id, userId, object :
            ChannelIdApiListener {
            override fun onSuccess(response: JSONObject, tokenId: String, channelId: String) {
                loadingDialog.dismiss()
                if (response.has("result")) {
                    val result = response.getJSONObject("result")
                    if (result.getBoolean("success")) {
                        viewModel.setChannelIdAndTokenId(channelIdValue = channelId, tokenIdValue = tokenId)
                        navController.navigate(R.id.action_doctorFragment_to_videoChatFragment)
                    } else {
//                        binding.root.snackbar("Something Went Wrong! ${result.getString("msg")}")
                        Popup(requireContext(),requireView(), "Something Went Wrong! ${result.getString("msg")}")
                    }
                } else {
//                    binding.root.snackbar("Something Went Wrong!")
                    Popup(requireContext(),requireView(), "Something Went Wrong!")
                }
            }

            override fun onFailure(error: VolleyError) {
                loadingDialog.dismiss()
//                binding.root.snackbar("Something went wrong! ${error.message}")
                Popup(requireContext(),requireView(), "Something Went Wrong! ${error.message}")
            }
        })
    }

    override fun onSuccess(response: JSONObject) {

    }

    override fun onFailure(error: VolleyError) {
//        Log.d(TAG, "Error ${error.message}")
        loadingDialog.dismiss()
        binding.root.snackbar("Something went wrong! ${error.message}")
    }

}