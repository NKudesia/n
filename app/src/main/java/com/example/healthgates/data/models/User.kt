package com.example.healthgates.data.models

data class User(
        var login: String = "",
        var name: String = "",
        var mobile: String = "",
        var gender: String = "",
        var height: String = "",
        var weight: String = "",
        var date_of_birth: String = "",
        var password: String = "",
        var nationality : String = "",
        var gov_source_id : String = "",
        var government_id : String = ""
)