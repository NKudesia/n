package com.example.healthgates.ui.fragments

import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.healthgates.R
import com.example.healthgates.data.models.Doctor
import com.example.healthgates.databinding.*
import com.example.healthgates.ui.base.BaseFragment
import com.example.healthgates.ui.viewmodel.PatientViewModel

class BookingConfirmationFragment : BaseFragment<FragmentSignupComplimenteryBinding, PatientViewModel>() {
    private lateinit var selectedDoctor: Doctor
    private lateinit var selectedDate: String
    private lateinit var selectedTime: String

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    )= FragmentSignupComplimenteryBinding.inflate(inflater, container, false)

    override fun getViewModel()= PatientViewModel::class.java

    override fun setupTheme() {

        selectedDoctor = viewModel.getSelectedDoctor()!!
        selectedDate = viewModel.getSelectedDate()!!
        selectedTime = viewModel.getSelectedTime()!!

        binding.tvSigupComplimentery.text = "Thank You for booking appointment on " + selectedDate + " at " + selectedTime + " with " + selectedDoctor.name

    }

    override fun setupClickListeners() {

        binding.tvOkBtn.setOnClickListener(){

            navController.navigate(R.id.action_bookingConfirmationFragment_to_homeFragment)
//            navController.popBackStack(R.id.homeFragment, false)
        }
    }


}