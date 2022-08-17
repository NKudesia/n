package com.example.healthgates.singleton

import android.app.Application
import android.content.Context
import com.example.healthgates.data.preferences.UserPreferences
import com.example.healthgates.utils.LocaleUtil

class MyApplication : Application() {

    companion object {
        private lateinit var mInstance: MyApplication
        fun getAppContext(): Context = mInstance.applicationContext
    }

    override fun onCreate() {
        super.onCreate()
        mInstance = this
    }

}