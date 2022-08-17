package com.example.healthgates.data.models

data class LabTest(
    val id: Int,
//    val physician_id: Any = 0,
//    val diagnosis: ArrayList<Diagnosis>
    val date: String = "",
    val test: String = "",
    val status: String = "",
    val report: String = ""
)


