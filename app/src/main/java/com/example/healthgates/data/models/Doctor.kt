package com.example.healthgates.data.models

data class Doctor(
        val id: Int,
        val user_id: Int,
        val profileImg: String = "",
        val name: String = "",
        val isOnline: Boolean = false,
        val speciality: Speciality,
//        val speciality: List<Int> = emptyList(),
        val nationality: String = "",
        val shortDescription: String = "",
        val description: String = "",
 )
