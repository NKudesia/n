package com.example.healthgates.data.models

data class Appointment(
    val id: Int,
    val physician_id: Int,
    val physician_user_id: Int,
    val physician_name: String = "",
    val physician_image: String = "",
    val speciality_id: Int,
    val speciality_name: String = "",
    val blood_group: String = "",
    val date: String = "",
    val from_slot: String = "",
    val to_slot: String = "",
    val temprature: String = "",
    val weight: String = "",
    val diastolic_blood_pressure: String = "",
    val systolic_blood_pressure: String = "",
    val heart_rate_per_minute: String = "",
    val state: String = "",
    val test_count: Int,
    val booked_online: Boolean,
    val lab_report_url: String,
    val prescription_report_url: String,
    val diagnosis: ArrayList<Diagnosis>
)
