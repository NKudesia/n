package com.example.healthgates.singleton;

import android.content.Context
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley

class VolleySingleton constructor(context: Context) {

    companion object {
        @Volatile
        private var INSTANCE: VolleySingleton? = null
        fun getInstance(context: Context) =
                INSTANCE ?: synchronized(this) {
                    INSTANCE ?: VolleySingleton(context).also {
                        INSTANCE = it
                    }
                }
    }

    private val requestQueue: RequestQueue by lazy {
        // applicationContext is key, it keeps you from leaking the
        // Activity or BroadcastReceiver if someone passes one in.
        Volley.newRequestQueue(context.applicationContext)
    }

    fun <T> addToRequestQueue(req: Request<T>) {
        val retryPolicy = DefaultRetryPolicy(0, -1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        req.retryPolicy = retryPolicy
//        req.setShouldCache(false)
//        requestQueue.cache.clear()
        requestQueue.add(req)
    }

}