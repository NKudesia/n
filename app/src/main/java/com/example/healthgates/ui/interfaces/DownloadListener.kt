package com.example.healthgates.ui.interfaces

interface DownloadListener {
    fun onSuccess(path: String)
    fun onFailure(error: String)
}