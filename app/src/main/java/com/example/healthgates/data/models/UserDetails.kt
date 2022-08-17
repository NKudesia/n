package com.example.healthgates.data.models

data class UserDetails(
        val id: Int = 0,
        val name: String = "",
        val family_member_ids: List<Int> = emptyList(),
        val gender: String = "",
        val age: String = "",
        val code: String = "",
        val mobile: String = "",
        val email: String = "",
        val insurance_ids: List<Int> = emptyList(),
        val gov_code: String = "",
        val height: Double = 0.0,
        val weight: Double = 0.0,
        val blood_group: String = "",
        val language: String = "",
        val insurance: String = "",
        val policy_number: String = "",
        val validity: String = "",
        val class_name: String = "",
        val bmi: Double = 0.0,
        val birthday: String = "",
        val image_1920: String = ""
)