package com.example.healthgates.data.repositories

import android.util.Log
import com.android.volley.Response
import com.android.volley.request.JsonObjectRequest
import com.example.healthgates.data.config.AppConfig
import com.example.healthgates.singleton.MyApplication
import com.example.healthgates.singleton.VolleySingleton
import com.example.healthgates.ui.interfaces.ApiListener
import com.example.healthgates.ui.interfaces.ApiListener2
import com.example.healthgates.ui.interfaces.ChannelIdApiListener
import org.json.JSONArray
import org.json.JSONObject

private const val TAG = "Doctor"

class DoctorRepository {

    fun searchReadDoctorDetails(userId: Int, listener: ApiListener) {
        val successListener = Response.Listener<JSONObject> { response ->
//            Log.d(TAG, "id $specialityId")
            listener.onSuccess(response)
        }
        val errorListener = Response.ErrorListener { error ->
            listener.onFailure(error)
        }

        val contextObject = JSONObject()
        with(contextObject) {
            put("lang", "en_US")
            put("tz", "Asia/Riyadh")
            put("uid", userId)
        }

        val paramsObject = JSONObject()
        with(paramsObject) {
            put("model", "hms.physician")
//            put("domain", JSONArray(arrayOf(JSONArray(arrayOf("specialty", "=", specialityId)))))
            put("fields", JSONArray(arrayOf("id", "user_id", "name", "specialty", "image_1920")))
            put("limit", AppConfig.SEARCH_LIMIT)
            put("sort", "")
            put("context", contextObject)
        }

        val jsonObject = JSONObject()
        with(jsonObject) {
            put("jsonrpc", AppConfig.JSON_RPC)
            put("method", AppConfig.METHOD)
            put("params", paramsObject)
            put("id", "505136376")
        }

        val jsonObjectRequest = object : JsonObjectRequest(Method.POST, AppConfig.SEARCH_READ_DOCTOR_DETAILS_URL, jsonObject, successListener, errorListener) {
            override fun getHeaders(): MutableMap<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["Accept"] = "application/json"
                params["Content-Type"] = "application/json"
                return params
            }
        }

        VolleySingleton.getInstance(MyApplication.getAppContext()).addToRequestQueue(jsonObjectRequest)
    }

    fun searchReadDoctorDetailsTwo(userId: Int, specialityId: Int, branchId: Int, mode: Int, listener: ApiListener) {
        val successListener = Response.Listener<JSONObject> { response ->
//            Log.d(TAG, "id $specialityId")
            listener.onSuccess(response)
        }
        val errorListener = Response.ErrorListener { error ->
            listener.onFailure(error)
        }

        val contextObject = JSONObject()
        with(contextObject) {
            put("lang", "en_US")
            put("tz", "Asia/Riyadh")
            put("uid", userId)
        }

        val paramsObject = JSONObject()
        with(paramsObject) {
            put("model", "hms.physician")
            if(mode == 0) {
                put(
                    "domain",
                    JSONArray(
                        arrayOf(
                            JSONArray(arrayOf("specialty", "=", specialityId)),
                            arrayOf("branch_id", "=", branchId)
                        )
                    )
                )
            }else{
                put(
                    "domain",
                    JSONArray(
                        arrayOf(
                            JSONArray(arrayOf("specialty", "=", specialityId))
                        )
                    )
                )
            }
            put("fields", JSONArray(arrayOf("id", "user_id", "name", "specialty", "image_1920")))
            put("limit", AppConfig.SEARCH_LIMIT)
            put("sort", "")
            put("context", contextObject)
        }

        val jsonObject = JSONObject()
        with(jsonObject) {
            put("jsonrpc", AppConfig.JSON_RPC)
            put("method", AppConfig.METHOD)
            put("params", paramsObject)
            put("id", "505136376")
        }

        val jsonObjectRequest = object : JsonObjectRequest(Method.POST, AppConfig.SEARCH_READ_DOCTOR_DETAILS_URL, jsonObject, successListener, errorListener) {
            override fun getHeaders(): MutableMap<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["Accept"] = "application/json"
                params["Content-Type"] = "application/json"
                return params
            }
        }

        VolleySingleton.getInstance(MyApplication.getAppContext()).addToRequestQueue(jsonObjectRequest)
    }

    fun readDoctorDetails(userId: Int, doctorId: Int, listener: ApiListener) {
        val successListener = Response.Listener<JSONObject> { response ->
            listener.onSuccess(response)
        }
        val errorListener = Response.ErrorListener { error ->
            listener.onFailure(error)
        }

        val contextObject = JSONObject()
        with(contextObject) {
            put("lang", "en_US")
            put("tz", "Asia/Kolkata")
            put("uid", userId)
        }

        val kwargsObject = JSONObject()
        with(kwargsObject) {
            put("context", contextObject)
        }

        val paramsObject = JSONObject()
        with(paramsObject) {
            put("args", JSONArray(arrayOf(
                    JSONArray(arrayOf(doctorId)),
                    JSONArray(arrayOf("id", "name", "specialty", "biography", "image_1920")))
            ))
            put("model", "hms.physician")
            put("method", "read")
            put("kwargs", kwargsObject)
        }

        val jsonObject = JSONObject()
        with(jsonObject) {
            put("jsonrpc", AppConfig.JSON_RPC)
            put("method", AppConfig.METHOD)
            put("params", paramsObject)
            put("id", 807170280)
        }

        val jsonObjectRequest = object : JsonObjectRequest(Method.POST, AppConfig.READ_DOCTOR_DETAILS_URL, jsonObject, successListener, errorListener) {
            override fun getHeaders(): MutableMap<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["Accept"] = "application/json"
                params["Content-Type"] = "application/json"
                return params
            }
        }

        VolleySingleton.getInstance(MyApplication.getAppContext()).addToRequestQueue(jsonObjectRequest)
    }

    fun getBookingSlots(doctorId: Int, date: String, listener: ApiListener2) {
        val successListener = Response.Listener<JSONObject> { response ->
//            Log.d(TAG, "id $specialityId")
            listener.onSuccess(response, 0)
        }
        val errorListener = Response.ErrorListener { error ->
            listener.onFailure(error)
        }

//        val contextObject = JSONObject()
//        with(contextObject) {
//            put("lang", "en_US")
//            put("tz", "Asia/Riyadh")
//            put("uid", userId)
//        }

        val paramsObject = JSONObject()
        with(paramsObject) {
            put("physician_id", doctorId)
            put("slot_date", date)
            put("limit", 1)
        }

        val jsonObject = JSONObject()
        with(jsonObject) {
            put("jsonrpc", AppConfig.JSON_RPC)
            put("method", AppConfig.METHOD)
            put("params", paramsObject)
            put("id", "505136376")
        }

        val jsonObjectRequest = object : JsonObjectRequest(Method.POST, AppConfig.GET_BOOKING_SLOTS_URL, jsonObject, successListener, errorListener) {
            override fun getHeaders(): MutableMap<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["Accept"] = "application/json"
                params["Content-Type"] = "application/json"
                return params
            }
        }

        VolleySingleton.getInstance(MyApplication.getAppContext()).addToRequestQueue(jsonObjectRequest)
    }

    fun getAllBranches(userId: Int, listener: ApiListener) {
            val successListener = Response.Listener<JSONObject> { response ->
//            Log.d(TAG, "id $specialityId")
                listener.onSuccess(response)
            }
            val errorListener = Response.ErrorListener { error ->
                listener.onFailure(error)
            }

            val contextObject = JSONObject()
            with(contextObject) {
                put("lang", "en_US")
                put("tz", "Asia/Riyadh")
                put("uid", userId)
            }

            val paramsObject = JSONObject()
            with(paramsObject) {
//                put("model", "res.branch")
////            put("domain", JSONArray(arrayOf(JSONArray(arrayOf("specialty", "=", specialityId)))))
//                put("fields", JSONArray(arrayOf("id", "name", "address", "telephone", "branch_logo")))
//                put("limit", AppConfig.SEARCH_LIMIT)
//                put("sort", "")
//                put("context", contextObject)
            }

            val jsonObject = JSONObject()
            with(jsonObject) {
                put("jsonrpc", AppConfig.JSON_RPC)
                put("method", AppConfig.METHOD)
                put("params", paramsObject)
                put("id", "505136376")
            }

            val jsonObjectRequest = object : JsonObjectRequest(Method.POST, AppConfig.GET_ALL_BRANCHES_URL, jsonObject, successListener, errorListener) {
                override fun getHeaders(): MutableMap<String, String> {
                    val params: MutableMap<String, String> = HashMap()
                    params["Accept"] = "application/json"
                    params["Content-Type"] = "application/json"
                    return params
                }
            }

            VolleySingleton.getInstance(MyApplication.getAppContext()).addToRequestQueue(jsonObjectRequest)
        }

    fun generateTokenAndChannelId(doctorId: Int, patientId: Int, listener: ChannelIdApiListener) {
        val successListener = Response.Listener<JSONObject> { response ->
            updateChannelIdAndPatientId(doctorId, patientId, response, listener)
        }
        val errorListener = Response.ErrorListener { error ->
            listener.onFailure(error)
        }

        val jsonObject = JSONObject()
        with(jsonObject) {
            put("jsonrpc", AppConfig.JSON_RPC)
            put("method", AppConfig.METHOD)
            put("params", JSONObject())
            put("id", 129575637)
        }

        val jsonObjectRequest = object : JsonObjectRequest(Method.POST, AppConfig.GENERATE_TOKEN_CHANNEL_ID_URL, jsonObject, successListener, errorListener) {
            override fun getHeaders(): MutableMap<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["Accept"] = "application/json"
                params["Content-Type"] = "application/json"
                return params
            }
        }
        VolleySingleton.getInstance(MyApplication.getAppContext()).addToRequestQueue(jsonObjectRequest)
    }

    private fun updateChannelIdAndPatientId(doctorId: Int, patientId: Int, responseResult: JSONObject, listener: ChannelIdApiListener) {
        val result = responseResult.getJSONObject("result")
        val channelId = result.getString("channel_id")
        val tokenId = result.getString("token_id")

        val successListener = Response.Listener<JSONObject> { response ->
            listener.onSuccess(response, tokenId, channelId)
        }
        val errorListener = Response.ErrorListener { error ->
            listener.onFailure(error)
        }


        val paramsObject = JSONObject()
        with(paramsObject) {
            put("user_id", doctorId)
            put("channel_id", channelId)
            put("patient_id", patientId)
            put("token_id", tokenId)
        }

        val jsonObject = JSONObject()
        with(jsonObject) {
            put("jsonrpc", AppConfig.JSON_RPC)
            put("method", AppConfig.METHOD)
            put("params", paramsObject)
            put("id", 807170280)
        }

        val jsonObjectRequest = object : JsonObjectRequest(Method.POST, AppConfig.UPDATE_CHANNEL_ID_PATIENT_ID_URL, jsonObject, successListener, errorListener) {
            override fun getHeaders(): MutableMap<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["Accept"] = "application/json"
                params["Content-Type"] = "application/json"
                return params
            }
        }

        VolleySingleton.getInstance(MyApplication.getAppContext()).addToRequestQueue(jsonObjectRequest)
    }

    fun incomingCallToDoctor(doctorId: Int, listener: ApiListener) {
        val successListener = Response.Listener<JSONObject> { response ->
            listener.onSuccess(response)
        }
        val errorListener = Response.ErrorListener { error ->
            listener.onFailure(error)
        }

        val paramsObject = JSONObject()
        with(paramsObject) {
            put("user_id", doctorId)
        }

        val jsonObject = JSONObject()
        with(jsonObject) {
            put("jsonrpc", AppConfig.JSON_RPC)
            put("method", AppConfig.METHOD)
            put("params", paramsObject)
            put("id", 129575637)
        }

        val jsonObjectRequest = object : JsonObjectRequest(Method.POST, AppConfig.INCOMING_CALL_TO_DOCTOR_URL, jsonObject, successListener, errorListener) {
            override fun getHeaders(): MutableMap<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["Accept"] = "application/json"
                params["Content-Type"] = "application/json"
                return params
            }
        }
        VolleySingleton.getInstance(MyApplication.getAppContext()).addToRequestQueue(jsonObjectRequest)
    }


    fun disconnectCall(doctorId: Int, listener: ApiListener) {
        val successListener = Response.Listener<JSONObject> { response ->
            listener.onSuccess(response)
        }
        val errorListener = Response.ErrorListener { error ->
            listener.onFailure(error)
        }

        val paramsObject = JSONObject()
        with(paramsObject) {
            put("user_id", doctorId)
        }

        val jsonObject = JSONObject()
        with(jsonObject) {
            put("jsonrpc", AppConfig.JSON_RPC)
            put("method", AppConfig.METHOD)
            put("params", paramsObject)
            put("id", 129575637)
        }

        val jsonObjectRequest = object : JsonObjectRequest(Method.POST, AppConfig.INCOMING_CALL_TO_DOCTOR_URL, jsonObject, successListener, errorListener) {
            override fun getHeaders(): MutableMap<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["Accept"] = "application/json"
                params["Content-Type"] = "application/json"
                return params
            }
        }
        VolleySingleton.getInstance(MyApplication.getAppContext()).addToRequestQueue(jsonObjectRequest)
    }

}