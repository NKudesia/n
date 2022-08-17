package com.example.healthgates.ui.fragments

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.VolleyLog
import com.android.volley.error.VolleyError
import com.example.healthgates.R
import com.example.healthgates.data.models.Doctor
import com.example.healthgates.data.models.TimeSlot
import com.example.healthgates.databinding.FragmentBookAppointmentTimeBinding
import com.example.healthgates.ui.adapter.TimeSlotAdapter
import com.example.healthgates.ui.adapter.TimeSlotInterface
import com.example.healthgates.ui.base.BaseFragment
import com.example.healthgates.ui.interfaces.ApiListener2
import com.example.healthgates.ui.viewmodel.PatientViewModel
import com.example.healthgates.utils.Popup
import com.example.healthgates.utils.loadImage
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class BookAppointmentTimeFragment : BaseFragment<FragmentBookAppointmentTimeBinding, PatientViewModel>(),
    TimeSlotInterface, ApiListener2 {

    private lateinit var timeSlotAdapter: TimeSlotAdapter
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var selectedDoctor: Doctor
    private lateinit var selectedDate: String
    private var timeSlot: TimeSlot? = null
    private var patientId: Int = 2

    private val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US)
    private val dateFormat2 = SimpleDateFormat("dd-MM-yyyy", Locale.US)
    private val dateFormat3 = SimpleDateFormat("hh:mm a", Locale.US)

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    )= FragmentBookAppointmentTimeBinding.inflate(inflater, container, false)

    override fun getViewModel()= PatientViewModel::class.java

    override fun setupTheme() {

        lifecycleScope.launch(Dispatchers.Main) {
            patientId= userPreferences.patientId.first()!!
        }

        selectedDoctor = viewModel.getSelectedDoctor()!!
        selectedDate = viewModel.getSelectedDate()!!

        timeSlotAdapter = TimeSlotAdapter(this)
        binding.recyclerView.adapter = timeSlotAdapter
        binding.recyclerView.layoutManager = LinearLayoutManager(activity)

        updateUI(selectedDoctor)

        viewModel.getBookingSlots(selectedDoctor.id, selectedDate, this@BookAppointmentTimeFragment)

    }

    override fun setupClickListeners() {
        println(dateFormat.format(Date()))
        val dd = Date();
//        dateFormat.format(Date()),
        val bookedOnline = viewModel.getBookedOnline()
        binding.tvSubmit.setOnClickListener(){
            if(timeSlot != null) {
                viewModel.bookAppointment(
                    patientId,
                    selectedDoctor.id,
                    dateFormat.format(Date()),
                    timeSlot!!.id,
                    bookedOnline,
                    this@BookAppointmentTimeFragment
                )
            }else{
//                binding.root.snackbar("Please select a slot")
                Popup(requireContext(),requireView(), "Please select a slot")
            }
        }

    }


    private fun updateUI(doctor: Doctor) {
        with(binding) {
            tvName.text = doctor.name
            tvSpeciality.text = String.format(root.context.getString(R.string.specialist), selectedDoctor.speciality.name)
            ivProfile.loadImage(selectedDoctor.profileImg)
        }
    }

    override fun onViewClick(timeSlot: TimeSlot) {
        this.timeSlot = timeSlot
    }


    override fun onSuccess(response: JSONObject, type: Int) {
        Log.d(VolleyLog.TAG, "onSuccess: $response")
        if(type == 0) {
            if (response.has("result") && response["result"] is JSONArray) {
                    val result = response.getJSONArray("result")
//            val length = result.getInt("length")
                    val slots = result.getJSONObject(0).getJSONArray("slot_line")
                    var list: ArrayList<TimeSlot> = ArrayList()

                    val type = object : TypeToken<ArrayList<TimeSlot>>() {}.type

                    list = Gson().fromJson(slots.toString(), type)

                    timeSlotAdapter.updateList(list)

            } else {
//                Log.d(
//                    VolleyLog.TAG,
//                    "Error: ${response.getJSONObject("error").getString("message")}"
//                )
            }
        }else{
            if (response.has("result")) {
                val date = dateFormat.parse(this.timeSlot!!.from_slot)
                viewModel.setSelectedDate(dateFormat2.format(date))
                viewModel.setSelectedTime(dateFormat3.format(date))
                navController.navigate(R.id.action_bookAppointmentTimeFragment_to_bookingConfirmationFragment)
            }
            }
    }

    override fun onFailure(error: VolleyError) {
        Log.d(VolleyLog.TAG, "Error ${error.message}")
//        binding.root.snackbar("Something went wrong! ${error.message}")
        Popup(requireContext(),requireView(), "Something Went Wrong! ${error.message}")
    }


}