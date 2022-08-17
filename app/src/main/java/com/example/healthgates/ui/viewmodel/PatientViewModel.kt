package com.example.healthgates.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.healthgates.data.models.*
import com.example.healthgates.data.repositories.DoctorRepository
import com.example.healthgates.data.repositories.PatientRepository
import com.example.healthgates.ui.interfaces.ApiListener
import com.example.healthgates.ui.interfaces.ApiListener2
import com.example.healthgates.ui.interfaces.ChannelIdApiListener
import java.util.*
import kotlin.collections.ArrayList

class PatientViewModel : ViewModel() {

    private val patientRepository : PatientRepository = PatientRepository()
    private val doctorRepository: DoctorRepository = DoctorRepository()
    private var currentUser: MutableLiveData<UserDetails> = MutableLiveData()
    private var mode: Int = -1
    private var selectedDoctor: Doctor? = null
    private var selectedDate: String = ""
    private var selectedTime: String = ""
    private var selectedBranch: Branch? = null
    private var selectedAppointment: Appointment? = null
    private var labTestMode: Int = 0
    private var doctorListMode: Int = 0
    private var mapUrl: String = ""
    private var bookedOnline: Boolean = true
    private var channelId: String? = null
    private var tokenId: String? = null
    private var specialityListLiveData: MutableLiveData<ArrayList<Speciality>> = MutableLiveData()

    fun getCurrentUser(userId: Int): LiveData<UserDetails> {
        currentUser = patientRepository.getCurrentUser(userId)
        return currentUser
    }

    fun searchReadSpecialty(userId: Int): LiveData<ArrayList<Speciality>> {
        specialityListLiveData = patientRepository.loadSpecialityList(userId)
        return specialityListLiveData
    }

    fun searchReadDoctorDetails(userId: Int, listener: ApiListener) = doctorRepository.searchReadDoctorDetails(userId,  listener)

    fun searchReadDoctorDetailsTwo(userId: Int, specialityId: Int, branchId: Int, mode: Int, listener: ApiListener) = doctorRepository.searchReadDoctorDetailsTwo(userId, specialityId, branchId, mode, listener)

    fun readDoctorDetails(userId: Int, doctorId: Int, listener: ApiListener) = doctorRepository.readDoctorDetails(userId, doctorId, listener)

    fun getBookingSlots(doctorId: Int, date: String, listener: ApiListener2) = doctorRepository.getBookingSlots(doctorId, date, listener)

    fun bookAppointment(userId: Int, doctorId: Int, date: String, slot_id: Int, booked_online: Boolean, listener: ApiListener2) = patientRepository.bookAppointment(userId, doctorId, date, slot_id, booked_online, listener)

    fun getAllBranches(userId: Int, listener: ApiListener) = doctorRepository.getAllBranches(userId,  listener)

    fun getAppointmentDetails(userId: Int, listener: ApiListener) = patientRepository.getAppointmentDetails(userId,  listener)

    fun getAllLabTestResults(userId: Int, listener: ApiListener) = patientRepository.getAllLabTestResults(userId, listener)

    fun getAllPrescriptions(userId: Int, listener: ApiListener) = patientRepository.getAllPrescriptions(userId, listener)

    fun getAllRadTestResults(userId: Int, listener: ApiListener) = patientRepository.getAllRadTestResults(userId, listener)

    fun getLabTestResults(userId: Int, appointmentId: Int, listener: ApiListener) = patientRepository.getLabTestResults(userId, appointmentId,  listener)

    fun getSelectedDoctor(): Doctor? = selectedDoctor

    fun setSelectedDoctor(updated: Doctor) {
        selectedDoctor = updated
    }

    fun getSelectedDate(): String? = selectedDate

    fun setSelectedDate(date: String){
        selectedDate = date
    }

    fun getSelectedTime(): String? = selectedTime

    fun setSelectedTime(time: String){
        selectedTime = time
    }

    fun getSelectedBranch(): Branch? = selectedBranch

    fun setSelectedBranch(branch: Branch){
        selectedBranch = branch
    }

    fun getSelectedAppointment(): Appointment? = selectedAppointment

    fun setSelectedAppointment(appointment: Appointment){
        selectedAppointment = appointment
    }

    fun getLabTestMode(): Int? = labTestMode

    fun setLabTestMode(labTestMode: Int){
        this.labTestMode = labTestMode
    }

    fun getDoctorListMode(): Int? = doctorListMode

    fun setDoctorListMode(doctorListMode: Int){
        this.doctorListMode = doctorListMode
    }

    fun getMapUrl(): String? = mapUrl

    fun setMapUrl(mapUrl: String){
        this.mapUrl = mapUrl
    }

    fun getBookedOnline(): Boolean = bookedOnline

    fun setBookedOnline(bookedOnline: Boolean){
        this.bookedOnline = bookedOnline
    }

    fun getChannelId(): String = channelId!!

    fun getTokenId(): String = tokenId!!

    fun setChannelIdAndTokenId(channelIdValue: String, tokenIdValue: String) {
        channelId = channelIdValue
        tokenId = tokenIdValue
    }

    fun generateTokenAndChannelId(doctorId: Int, patientId: Int, listener: ChannelIdApiListener) = doctorRepository.generateTokenAndChannelId(doctorId, patientId, listener)

    fun incomingCallToDoctor(doctorId: Int, listener: ApiListener) = doctorRepository.incomingCallToDoctor(doctorId, listener)

    fun disconnectCall(doctorId: Int, listener: ApiListener) = doctorRepository.disconnectCall(doctorId, listener)
}