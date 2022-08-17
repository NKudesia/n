package com.example.healthgates.data.repositories

import com.android.volley.Response
import com.android.volley.request.JsonObjectRequest
import com.example.healthgates.data.config.AppConfig
import com.example.healthgates.data.models.User
import com.example.healthgates.singleton.MyApplication
import com.example.healthgates.singleton.VolleySingleton
import com.example.healthgates.ui.interfaces.ApiListener
import org.json.JSONObject

class LoginRepository {

    fun login(user: User, listener: ApiListener) {
        val successListener = Response.Listener<JSONObject> { response ->
            listener.onSuccess(response)
        }
        val errorListener = Response.ErrorListener { error ->
            listener.onFailure(error)
        }
        val paramsObject = JSONObject()
        with(paramsObject) {
            put("db", AppConfig.DB)
            put("login", user.login)
            put("password", user.password)
        }

        val jsonObject = JSONObject()
        with(jsonObject) {
            put("jsonrpc", AppConfig.JSON_RPC)
            put("method", AppConfig.METHOD)
            put("params", paramsObject)
            put("id", 505136376)
        }

        val jsonObjectRequest = object : JsonObjectRequest(Method.POST, AppConfig.LOGIN_URL, jsonObject, successListener, errorListener) {
            override fun getHeaders(): MutableMap<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["Accept"] = "application/json"
                params["Content-Type"] = "application/json"
                return params
            }
        }

        VolleySingleton.getInstance(MyApplication.getAppContext()).addToRequestQueue(jsonObjectRequest)
    }

    fun signUp(user: User, listener: ApiListener) {
        val successListener = Response.Listener<JSONObject> { response ->
            listener.onSuccess(response)
        }
        val errorListener = Response.ErrorListener { error ->
            listener.onFailure(error)
        }
        val paramsObject = JSONObject()
        with(paramsObject) {
            put("login", user.login)
            put("name", user.name)
            put("mobile", user.mobile)
            put("nationality", user.nationality)
            put("gender", user.gender)
            put("password", user.password)
        }

        val jsonObject = JSONObject()
        with(jsonObject) {
            put("jsonrpc", AppConfig.JSON_RPC)
            put("method", AppConfig.METHOD)
            put("params", paramsObject)
            put("id", 505136376)
        }

        val jsonObjectRequest = object : JsonObjectRequest(Method.POST, AppConfig.SIGNUP_URL, jsonObject, successListener, errorListener) {
            override fun getHeaders(): MutableMap<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["Accept"] = "application/json"
                params["Content-Type"] = "application/json"
                return params
            }
        }
        VolleySingleton.getInstance(MyApplication.getAppContext()).addToRequestQueue(jsonObjectRequest)
    }

    fun resetPassword(email: String, listener: ApiListener) {
        val successListener = Response.Listener<JSONObject> { response ->
            listener.onSuccess(response)
        }
        val errorListener = Response.ErrorListener { error ->
            listener.onFailure(error)
        }
        val paramsObject = JSONObject()
        with(paramsObject) {
            put("email", email)

        }

        val jsonObject = JSONObject()
        with(jsonObject) {
            put("jsonrpc", AppConfig.JSON_RPC)
            put("method", AppConfig.METHOD)
            put("params", paramsObject)
            put("id", "505136376")
        }

        val jsonObjectRequest = object : JsonObjectRequest(Method.POST, AppConfig.RESET_PASSWORD_URL, jsonObject, successListener, errorListener) {
            override fun getHeaders(): MutableMap<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["Accept"] = "application/json"
                return params
            }
        }

        VolleySingleton.getInstance(MyApplication.getAppContext()).addToRequestQueue(jsonObjectRequest)
    }

}