package com.example.healthgates.data.models

data class RecentVisit(
        val profileImg: String,
        val name: String,
        val specialist: String,
        val nationality: String,
        val shortDescription: String,
        val description: String,
        val visitTime: String,
        val visitDate: String,
)