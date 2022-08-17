package com.example.healthgates.ui.interfaces

import com.android.volley.error.VolleyError
import org.json.JSONObject

interface ApiListener {
    fun onSuccess(response: JSONObject)
    fun onFailure(error: VolleyError)
}