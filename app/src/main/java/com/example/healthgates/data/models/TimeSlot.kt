package com.example.healthgates.data.models

data class TimeSlot (
    val id: Int,
    val name: String,
    val from_slot: String,
    val remaining_limit: Int
)