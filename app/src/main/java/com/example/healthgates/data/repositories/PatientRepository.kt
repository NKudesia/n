package com.example.healthgates.data.repositories

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.volley.Response
import com.android.volley.request.JsonObjectRequest
import com.example.healthgates.data.config.AppConfig
import com.example.healthgates.data.models.Appointment
import com.example.healthgates.data.models.LabTest
import com.example.healthgates.data.models.Speciality
import com.example.healthgates.data.models.UserDetails
import com.example.healthgates.singleton.MyApplication
import com.example.healthgates.singleton.VolleySingleton
import com.example.healthgates.ui.interfaces.ApiListener
import com.example.healthgates.ui.interfaces.ApiListener2
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.json.JSONArray
import org.json.JSONObject
import java.nio.charset.Charset

private const val TAG = "Patient"

class PatientRepository {

    private var user: UserDetails? = null
    private val userLiveData: MutableLiveData<UserDetails> = MutableLiveData()
    private var specialityList: ArrayList<Speciality> = ArrayList()
    private val specialityListLiveData: MutableLiveData<ArrayList<Speciality>> = MutableLiveData()

    fun getCurrentUser(userId: Int): MutableLiveData<UserDetails> {
//        if (user == null) {
            loadUserDetails(userId)
//        }
//        userLiveData.value = user
        return userLiveData
    }

    @SuppressLint("LogNotTimber")
    private fun loadUserDetails(partnerId: Int) {
        val successListener = Response.Listener<JSONObject> { response ->
            Log.d(TAG, "userId $partnerId; onSuccess: $response")
            if (response.has("result")) {
//                val resultArray = response.getJSONArray("result")

                val result = response?.getJSONArray("result")

                if (result != null) {

//                    val records = result.getJSONArray("records");
//
//                    if (records.length() > 0) {

                        val record = result.getJSONObject(0)

                        if (record != null) {

                            val type = object : TypeToken<UserDetails>() {}.type

                            user = Gson().fromJson(record.toString(), type)

//                            user = UserDetails(
//                                id = record.getInt("id"),
//                                name = record.getString("name"),
//                                image_1920 = record.getString("image_1920"),
//                                code = record.getString("code"),
//                                email = record.getString("email"),
//                                mobile = record.getString("mobile"),
//                                height = record.getDouble("height"),
//                                weight = record.getDouble("weight"),
//                                blood_group = record.getString("blood_group"),
//                                language = record.getString("language"),
//                                insurance = record.getString("insurance"),
//                                policy_number = record.getString("policy_number"),
//                                validity = record.getString("validity"),
//                                class_name = record.getString("class_name")
//                            )
                            userLiveData.postValue(user!!)
                        }
                    }
//                }

            } else {
                Log.d(TAG, "Error: ${response.getJSONObject("error").getString("message")}")
            }
        }
        val errorListener = Response.ErrorListener { error ->
            Log.d(TAG, "Error: ${error.message}")
        }

//        val contextObject = JSONObject()
//        with(contextObject) {
//            put("lang", "en_US")
//            put("tz", "Asia/Riyadh")
//            put("uid", userId)
//        }

        val paramsObject = JSONObject()
        with(paramsObject) {
//            put("model", "hms.patient")
//            put("domain",
//                JSONArray(arrayOf(
//                    JSONArray(arrayOf("id", "=", userId)))))
//            put("fields",
//            JSONArray(arrayOf("id", "name", "image_1920")))
//            put("limit", 80)
//            put("sort", "")
//            put("context", contextObject)
//            put("user_id", userId)
            put("partner_id", partnerId)
        }

        val jsonObject = JSONObject()
        with(jsonObject) {
            put("jsonrpc", AppConfig.JSON_RPC)
            put("method", AppConfig.METHOD)
            put("params", paramsObject)
            put("id", 505136376)
        }

        val jsonObjectRequest = object : JsonObjectRequest(Method.POST, AppConfig.PATIENT_DETIALS_URL, jsonObject, successListener, errorListener) {
            override fun getHeaders(): MutableMap<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["Accept"] = "application/json"
                params["Content-Type"] = "application/json"
                return params
            }
        }

        VolleySingleton.getInstance(MyApplication.getAppContext()).addToRequestQueue(jsonObjectRequest)
    }


    fun getPatientDetails(userId: Int, listener: ApiListener) {

        val successListener = Response.Listener<JSONObject> { response ->
            listener.onSuccess(response)
        }
        val errorListener = Response.ErrorListener { error ->
            listener.onFailure(error)
        }

        val paramsObject = JSONObject()
        with(paramsObject) {
            put("model", "hms.patient")
            put("domain", JSONArray(arrayOf(
                    JSONArray(arrayOf("user_id", "=", userId)))
            ))
            put("fields", JSONArray(arrayOf("id", "name")))
        }

        val jsonObject = JSONObject()
        with(jsonObject) {
            put("jsonrpc", AppConfig.JSON_RPC)
            put("method", AppConfig.METHOD)
            put("params", paramsObject)
            put("id", 807170280)
        }

        val jsonObjectRequest = object : JsonObjectRequest(Method.POST, AppConfig.SEARCH_READ_PATIENT_DETAILS_URL, jsonObject, successListener, errorListener) {
            override fun getHeaders(): MutableMap<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["Accept"] = "application/json"
                params["Content-Type"] = "application/json"
                return params
            }
        }

        VolleySingleton.getInstance(MyApplication.getAppContext()).addToRequestQueue(jsonObjectRequest)
    }


    fun loadSpecialityList(userId: Int): MutableLiveData<ArrayList<Speciality>> {
        if (specialityList.isEmpty()) {
            searchReadSpecialty(userId)
        }
        specialityListLiveData.value = specialityList
        return specialityListLiveData
    }

    private fun searchReadSpecialty(userId: Int) {
        val successListener = Response.Listener<JSONObject> { response ->
            Log.d(TAG, "onSuccess: $response")

            if (response.has("result")) {
                val result = response.getJSONObject("result")
                val length = result.getInt("length")
                val records = result.getJSONArray("records")

//                for (i in 0 until length) {
//                    specialityList.add(
//                            Speciality(id = records.getJSONObject(i).getInt("id"), name = records.getJSONObject(i).getString("name"))
//                    )
//                }

                val type = object : TypeToken<ArrayList<Speciality>>() {}.type
                specialityList = Gson().fromJson(records.toString(), type)

                specialityListLiveData.postValue(specialityList)

            } else {
                try {
                    Log.d(TAG, "Error: ${response.getJSONObject("error").getString("message")}")
                } catch (e: Exception) {
                    Log.d(TAG, "Error: ${e.message}}")
                }
            }
        }
        val errorListener = Response.ErrorListener { error ->
            Log.d(TAG, "Error: ${error.message}")
            try {
                val responseBody = String(error.networkResponse.data, Charset.forName("utf-8"))
                Log.d(TAG, "error $responseBody")
                val data = JSONObject(responseBody)
                Log.d(TAG, "error $data")
                val errors = data.getJSONArray("errors")
                Log.d(TAG, "error $errors")
                val jsonMessage = errors.getJSONObject(0)
                Log.d(TAG, "error $jsonMessage")
                val message = jsonMessage.getString("message")
                Log.d(TAG, "error $message")

            } catch (e: Exception) {
            }
        }

        val contextObject = JSONObject()
        with(contextObject) {
            put("lang", "en_US")
            put("tz", "Asia/Riyadh")
            put("uid", userId)
        }

        val paramsObject = JSONObject()
        with(paramsObject) {
            put("model", "physician.specialty")
            put("domain", JSONArray(arrayOf(arrayOf("show_in_app","=",true))))
            put("fields", JSONArray(arrayOf("id", "name")))
            put("limit", 200)
            put("sort", "")
            put("context", contextObject)
        }

        val jsonObject = JSONObject()
        with(jsonObject) {
            put("jsonrpc", AppConfig.JSON_RPC)
            put("method", AppConfig.METHOD)
            put("params", paramsObject)
            put("id", 505136376)
        }
        Log.d(TAG, "Json: $jsonObject")

        val jsonObjectRequest = object : JsonObjectRequest(Method.POST, AppConfig.SEARCH_READ_SPECIALTY_URL, jsonObject, successListener, errorListener) {
            override fun getHeaders(): MutableMap<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["Accept"] = "application/json"
                params["Content-Type"] = "application/json"
                return params
            }
        }

        VolleySingleton.getInstance(MyApplication.getAppContext()).addToRequestQueue(jsonObjectRequest)
    }


    fun searchReadPatientDetails(listener: ApiListener) {
        val successListener = Response.Listener<JSONObject> { response ->
            listener.onSuccess(response)
        }
        val errorListener = Response.ErrorListener { error ->
            listener.onFailure(error)
        }

        val contextObject = JSONObject()
        with(contextObject) {
            put("lang", "en_US")
            put("tz", "Asia/Riyadh")
            put("uid", "2")
        }

        val paramsObject = JSONObject()
        with(paramsObject) {
            put("model", "hms.patient")
            put("fields", listOf("id", "name", "family_member_ids", "gender", "age", "height", "weight", "bmi", "image_1920"))
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

        val jsonObjectRequest = object : JsonObjectRequest(Method.POST, AppConfig.SEARCH_READ_PATIENT_DETAILS_URL, jsonObject, successListener, errorListener) {
            override fun getHeaders(): MutableMap<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["Accept"] = "application/json"
                params["Content-Type"] = "application/json"
                return params
            }
        }

        VolleySingleton.getInstance(MyApplication.getAppContext()).addToRequestQueue(jsonObjectRequest)
    }

    fun bookAppointment(userId: Int, doctorId: Int, date: String, slot_id: Int, booked_online: Boolean, listener: ApiListener2) {
        val successListener = Response.Listener<JSONObject> { response ->
            Log.d(TAG, "userId $userId; onSuccess: $response")
            listener.onSuccess(response, 1)
        }
        val errorListener = Response.ErrorListener { error ->
            Log.d(TAG, "Error: ${error.message}")
            listener.onFailure(error)
        }

        val contextObject = JSONObject()
        with(contextObject) {
            put("lang", "en_US")
            put("tz", "Asia/Riyadh")
            put("uid", userId)
        }

        val kwargsObject = JSONObject()
        with(kwargsObject) {
            put("context", contextObject)
        }

        val paramsObject = JSONObject()
        with(paramsObject) {
            put("args", JSONArray(arrayOf(
                    JSONObject()
                            .put("patient_id", userId)
                            .put("physician_id", doctorId)
                            .put("paid_by","by_cash")
                            .put("date", date)
                            .put("booked_online", booked_online)
                            .put("schedule_slot_id", slot_id)
            )))
            put("model", "hms.appointment")
            put("method", "create")
            put("kwargs", kwargsObject)
        }

        val jsonObject = JSONObject()
        with(jsonObject) {
            put("jsonrpc", AppConfig.JSON_RPC)
            put("method", AppConfig.METHOD)
            put("params", paramsObject)
            put("id", 807170280)
        }

        val jsonObjectRequest = object : JsonObjectRequest(Method.POST, AppConfig.BOOK_APPOINTMENT_URL, jsonObject, successListener, errorListener) {
            override fun getHeaders(): MutableMap<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["Accept"] = "application/json"
                params["Content-Type"] = "application/json"
                return params
            }
        }

        VolleySingleton.getInstance(MyApplication.getAppContext()).addToRequestQueue(jsonObjectRequest)
    }


    fun getAppointmentDetails(userId: Int, listener: ApiListener) {
        val successListener = Response.Listener<JSONObject> { response ->
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
//            put("model", "hms.appointment")
            put("patient_id", userId)
//            put("domain", JSONArray(arrayOf(JSONArray(arrayOf("patient_id", "=", userId)))))
//            put("fields", JSONArray(arrayOf("id","physician_id", "date","weight","patient_id","diastolic_blood_pressure","systolic_blood_pressure","heart_rate_per_minute","state", "test_count")))
//            put("limit", AppConfig.SEARCH_LIMIT)
//            put("sort", "")
//            put("context", contextObject)
        }

        val jsonObject = JSONObject()
        with(jsonObject) {
            put("jsonrpc", AppConfig.JSON_RPC)
            put("method", AppConfig.METHOD)
            put("params", paramsObject)
            put("id", "505136376")
        }

        val jsonObjectRequest = object : JsonObjectRequest(Method.POST, AppConfig.GET_APPOINTMENT_DETAILS_URL, jsonObject, successListener, errorListener) {
            override fun getHeaders(): MutableMap<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["Accept"] = "application/json"
                params["Content-Type"] = "application/json"
                return params
            }
        }

        VolleySingleton.getInstance(MyApplication.getAppContext()).addToRequestQueue(jsonObjectRequest)
    }


    fun getAllPrescriptions(userId: Int, listener: ApiListener) {
        val successListener = Response.Listener<JSONObject> { response ->
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
            put("patient_id", userId)
            put("limit", AppConfig.SEARCH_LIMIT)
        }

        val jsonObject = JSONObject()
        with(jsonObject) {
            put("jsonrpc", AppConfig.JSON_RPC)
            put("method", AppConfig.METHOD)
            put("params", paramsObject)
            put("id", "505136376")
        }

        val jsonObjectRequest = object : JsonObjectRequest(Method.POST, AppConfig.GET_ALL_PRESCRIPTIONS, jsonObject, successListener, errorListener) {
            override fun getHeaders(): MutableMap<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["Accept"] = "application/json"
                params["Content-Type"] = "application/json"
                return params
            }
        }

        VolleySingleton.getInstance(MyApplication.getAppContext()).addToRequestQueue(jsonObjectRequest)
    }


    fun getAllLabTestResults(userId: Int, listener: ApiListener) {
        val successListener = Response.Listener<JSONObject> { response ->
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
            put("patient_id", userId)
            put("limit", AppConfig.SEARCH_LIMIT)
        }

        val jsonObject = JSONObject()
        with(jsonObject) {
            put("jsonrpc", AppConfig.JSON_RPC)
            put("method", AppConfig.METHOD)
            put("params", paramsObject)
            put("id", "505136376")
        }

        val jsonObjectRequest = object : JsonObjectRequest(Method.POST, AppConfig.GET_ALL_LAB_TEST_RESULTS, jsonObject, successListener, errorListener) {
            override fun getHeaders(): MutableMap<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["Accept"] = "application/json"
                params["Content-Type"] = "application/json"
                return params
            }
        }

        VolleySingleton.getInstance(MyApplication.getAppContext()).addToRequestQueue(jsonObjectRequest)
    }


    fun getLabTestResults(userId: Int, appointmentId: Int, listener: ApiListener) {
        val successListener = Response.Listener<JSONObject> { response ->
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
            put("patient_id", userId)
            put("appointment_id", appointmentId)
            put("limit", AppConfig.SEARCH_LIMIT)
        }

        val jsonObject = JSONObject()
        with(jsonObject) {
            put("jsonrpc", AppConfig.JSON_RPC)
            put("method", AppConfig.METHOD)
            put("params", paramsObject)
            put("id", "505136376")
        }

        val jsonObjectRequest = object : JsonObjectRequest(Method.POST, AppConfig.GET_LAB_TEST_RESULTS, jsonObject, successListener, errorListener) {
            override fun getHeaders(): MutableMap<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["Accept"] = "application/json"
                params["Content-Type"] = "application/json"
                return params
            }
        }

        VolleySingleton.getInstance(MyApplication.getAppContext()).addToRequestQueue(jsonObjectRequest)
    }

    fun getAllRadTestResults(userId: Int, listener: ApiListener) {
        val successListener = Response.Listener<JSONObject> { response ->
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
            put("patient_id", userId)
            put("limit", AppConfig.SEARCH_LIMIT)
        }

        val jsonObject = JSONObject()
        with(jsonObject) {
            put("jsonrpc", AppConfig.JSON_RPC)
            put("method", AppConfig.METHOD)
            put("params", paramsObject)
            put("id", "505136376")
        }

        val jsonObjectRequest = object : JsonObjectRequest(Method.POST, AppConfig.GET_ALL_RAD_TEST_RESULTS, jsonObject, successListener, errorListener) {
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