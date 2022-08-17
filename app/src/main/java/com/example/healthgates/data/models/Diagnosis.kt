package com.example.healthgates.data.models

data class Diagnosis (
    val id: Int,
    val name: String = "",
    val normal_range: String = "",
    val result_type: String = ""
)