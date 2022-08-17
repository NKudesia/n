package com.example.healthgates.ui.fragments

import android.icu.text.DateFormat.getDateInstance
import android.text.format.DateFormat
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CalendarView
import com.example.healthgates.R
import com.example.healthgates.data.models.Doctor
import com.example.healthgates.databinding.FragmentBookAppointmentDateBinding
import com.example.healthgates.ui.base.BaseFragment
import com.example.healthgates.ui.viewmodel.PatientViewModel
import com.example.healthgates.utils.loadImage
import java.text.DateFormat.getDateInstance
import java.text.SimpleDateFormat
import java.util.*
import java.util.Calendar.LONG

class BookAppointmentDateFragment : BaseFragment<FragmentBookAppointmentDateBinding, PatientViewModel>() {

    private var userId: Int = 2
    private lateinit var selectedDoctor: Doctor

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    )= FragmentBookAppointmentDateBinding.inflate(inflater, container, false)

    override fun getViewModel()= PatientViewModel::class.java

    override fun setupTheme() {
        selectedDoctor = viewModel.getSelectedDoctor()!!
        binding.calendarView.minDate = System.currentTimeMillis() - 1000
        updateUI(selectedDoctor)
    }

    override fun setupClickListeners() {

        binding.calendarView.setOnDateChangeListener { calView: CalendarView, year: Int, month: Int, dayOfMonth: Int ->
            // Create calender object with which will have system date time.
            val calender: Calendar = Calendar.getInstance()

            // Set attributes in calender object as per selected date.
            calender.set(year, month, dayOfMonth)

            // Now set calenderView with this calender object to highlight selected date on UI.
            calView.setDate(calender.timeInMillis, true, true)
            Log.d("SelectedDate", "$dayOfMonth/${month + 1}/$year")
        }

        binding.tvNext.setOnClickListener(){

            // Fetch long milliseconds from calenderView.
            val dateMillis: Long = binding.calendarView.date

            // Create Date object from milliseconds.
            val date: Date = Date(dateMillis)

            // Get Date values and created formatted string date to show in Toast.
//              val selectedDayOfWeek = DateFormat.format("EEEE", date) as String // Monday
//            val selectedDay = DateFormat.format("dd", date) as String // 05
//            val selectedMonthString = DateFormat.format("MMM", date) as String // Jul
//            val selectedMonthNumber = DateFormat.format("MM", date) as String // 6 --> Month Code as Jan = 0 till Dec = 11.
//            val selectedYear = DateFormat.format("yyyy", date) as String // 2021

            val  simpleDateFormat = SimpleDateFormat("dd", Locale.US)
            val  simpleDateFormat2 = SimpleDateFormat("MM", Locale.US)
            val  simpleDateFormat3 = SimpleDateFormat("yyyy", Locale.US)

//            val day = simpleDateFormat.format(date)

            val selectedDay = simpleDateFormat.format(date) as String // 05
            val selectedMonthNumber = simpleDateFormat2.format(date) as String // 6 --> Month Code as Jan = 0 till Dec = 11.
            val selectedYear = simpleDateFormat3.format(date) as String // 2021

            val strFormattedSelectedDate = "$selectedYear-$selectedMonthNumber-$selectedDay"
            viewModel.setSelectedDate(strFormattedSelectedDate)
            navController.navigate(R.id.action_bookAppointmentDateFragment_to_bookAppointmentTimeFragment)
        }

    }

    private fun updateUI(doctor: Doctor) {
        with(binding) {
            tvName.text = doctor.name
            tvSpeciality.text = String.format(root.context.getString(R.string.specialist), selectedDoctor.speciality.name)
            ivProfile.loadImage(selectedDoctor.profileImg)
        }
    }


}















































































































//package com.example.healthgates.ui.fragments
//
//import android.text.format.DateFormat
//import android.util.Log
//import android.view.LayoutInflater
//import android.view.ViewGroup
//import android.widget.CalendarView
//import com.example.healthgates.R
//import com.example.healthgates.data.models.Doctor
//import com.example.healthgates.databinding.FragmentBookAppointmentDateBinding
//import com.example.healthgates.ui.base.BaseFragment
//import com.example.healthgates.ui.viewmodel.PatientViewModel
//import com.example.healthgates.utils.loadImage
//import java.util.*
//
//class BookAppointmentDateFragment : BaseFragment<FragmentBookAppointmentDateBinding, PatientViewModel>() {
//
//    private var userId: Int = 2
//    private lateinit var selectedDoctor: Doctor
//
//    override fun getFragmentBinding(
//        inflater: LayoutInflater,
//        container: ViewGroup?
//    )= FragmentBookAppointmentDateBinding.inflate(inflater, container, false)
//
//    override fun getViewModel()= PatientViewModel::class.java
//
//    override fun setupTheme() {
//        selectedDoctor = viewModel.getSelectedDoctor()!!
//        binding.calendarView.minDate = System.currentTimeMillis() - 1000
//        updateUI(selectedDoctor)
//    }
//
//    override fun setupClickListeners() {
//
//        binding.calendarView.setOnDateChangeListener { calView: CalendarView, year: Int, month: Int, dayOfMonth: Int ->
//            // Create calender object with which will have system date time.
//            val calender: Calendar = Calendar.getInstance()
//
//            // Set attributes in calender object as per selected date.
//            calender.set(year, month, dayOfMonth)
//
//            // Now set calenderView with this calender object to highlight selected date on UI.
//            calView.setDate(calender.timeInMillis, true, true)
//            Log.d("SelectedDate", "$dayOfMonth/${month + 1}/$year")
//        }
//
//        binding.tvNext.setOnClickListener(){
//
//            // Fetch long milliseconds from calenderView.
//            val dateMillis: Long = binding.calendarView.date
//
//            // Create Date object from milliseconds.
//            val date: Date = Date(dateMillis)
//
//            // Get Date values and created formatted string date to show in Toast.
//            val selectedDayOfWeek = DateFormat.format("EEEE", date) as String // Monday
//            val selectedDay = DateFormat.format("dd", date) as String // 05
//            val selectedMonthString = DateFormat.format("MMM", date) as String // Jul
//            val selectedMonthNumber = DateFormat.format("MM", date) as String // 6 --> Month Code as Jan = 0 till Dec = 11.
//            val selectedYear = DateFormat.format("yyyy", date) as String // 2021
//
//            val strFormattedSelectedDate = "$selectedYear-$selectedMonthNumber-$selectedDay"
//            viewModel.setSelectedDate(strFormattedSelectedDate)
//            navController.navigate(R.id.action_bookAppointmentDateFragment_to_bookAppointmentTimeFragment)
//        }
//
//    }
//
//    private fun updateUI(doctor: Doctor) {
//        with(binding) {
//            tvName.text = doctor.name
//            tvSpeciality.text = String.format(root.context.getString(R.string.specialist), selectedDoctor.speciality.name)
//            ivProfile.loadImage(selectedDoctor.profileImg)
//        }
//    }
//
//
//}