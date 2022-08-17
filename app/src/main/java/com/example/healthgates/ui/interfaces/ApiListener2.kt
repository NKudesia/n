package com.example.healthgates.ui.interfaces

import com.android.volley.error.VolleyError
import org.json.JSONObject

interface ApiListener2 {
    fun onSuccess(response: JSONObject, type: Int)
    fun onFailure(error: VolleyError)
}