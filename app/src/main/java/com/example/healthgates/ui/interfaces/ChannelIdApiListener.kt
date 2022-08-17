package com.example.healthgates.ui.interfaces

import com.android.volley.error.VolleyError
import org.json.JSONObject

interface ChannelIdApiListener {
    fun onSuccess(response: JSONObject, tokenId: String, channelId: String)
    fun onFailure(error: VolleyError)
}