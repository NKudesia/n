package com.example.healthgates.ui.fragments

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.VolleyLog
import com.android.volley.error.VolleyError
import com.example.healthgates.R
import com.example.healthgates.data.models.Appointment
import com.example.healthgates.data.models.Branch
import com.example.healthgates.data.models.TimeSlot
import com.example.healthgates.data.models.UserDetails
import com.example.healthgates.databinding.FragmentAppointmentsBinding
import com.example.healthgates.databinding.FragmentHomeBinding
import com.example.healthgates.ui.adapter.AppointmentAdapter
import com.example.healthgates.ui.adapter.AppointmentInterface
import com.example.healthgates.ui.base.BaseFragment
import com.example.healthgates.ui.interfaces.ApiListener
import com.example.healthgates.ui.viewmodel.PatientViewModel
import com.example.healthgates.utils.Popup
import com.example.healthgates.utils.loadImage
import com.example.healthgates.utils.snackbar
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject

class AppointmentsFragment : BaseFragment<FragmentAppointmentsBinding, PatientViewModel>(), ApiListener,
    AppointmentInterface {

    private lateinit var appointmentAdapter: AppointmentAdapter
    private lateinit var linearLayoutManager : LinearLayoutManager
    private var userId = 2
    private var list: ArrayList<Appointment> = ArrayList()

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) =  FragmentAppointmentsBinding.inflate(inflater, container, false)

    override fun getViewModel()= PatientViewModel::class.java

    override fun setupTheme() {

        lifecycleScope.launch(Dispatchers.Main){
            userId = userPreferences.patientId.first()!!
            viewModel.getCurrentUser(userPreferences.userId.first()!!).observe(viewLifecycleOwner,{
                it?.let { userDetails ->
                    updateUI(userDetails)
                }
            })
            viewModel.getAppointmentDetails(userId, this@AppointmentsFragment)
        }

        appointmentAdapter = AppointmentAdapter(this)
        linearLayoutManager = LinearLayoutManager(activity)
        binding.recyclerView.adapter = appointmentAdapter
        binding.recyclerView.layoutManager = linearLayoutManager

       }


    override fun setupClickListeners() {

//        binding.cardView.setOnClickListener(){
////            navController.navigate(R.id.action_homeFragment_to_profileFragment)
//        }

        binding.btnAppointment.setOnClickListener(){
            if(navController.currentDestination!!.id != R.id.viewDoctorsFragment) {
                viewModel.setDoctorListMode(1)
                navController.navigate(R.id.viewDoctorsFragment)
            }
        }

    }

    override fun onViewClick(appointment: Appointment) {
            viewModel.setSelectedAppointment(appointment)
            navController.navigate(R.id.action_appointmentsFragment_to_appointmentDetailsFragment)
    }

    private fun updateUI(userDetails: UserDetails){
        binding.tvName.text = userDetails.name
        binding.ivProfile.loadImage(userDetails.image_1920)
    }

    override fun onSuccess(response: JSONObject) {
        Log.d(VolleyLog.TAG, "onSuccess: $response")
        if (response.has("result")) {
            if (response.get("result") is JSONArray) {
                val result = response.getJSONArray("result")
//            val records = result.getJSONArray("records")
                val type = object : TypeToken<ArrayList<Appointment>>() {}.type

                list = Gson().fromJson(result.toString(), type)

                if(list.size == 0){
                    Popup(requireContext(),requireView(), resources.getString(R.string.dont_have_any_records))
                }else{
                    appointmentAdapter.updateList(list)
                }

            }else{
                Popup(requireContext(),requireView(), resources.getString(R.string.dont_have_any_records))
            }

        } else {
            Log.d(VolleyLog.TAG, "Error: ${response.getJSONObject("error").getString("message")}")
        }
    }

    override fun onFailure(error: VolleyError) {
        Log.d(VolleyLog.TAG, "Error ${error.message}")
//        binding.root.snackbar("Something went wrong! ${error.message}")
        Popup(requireContext(),requireView(), "Something Went Wrong! ${error.message}")
    }

}