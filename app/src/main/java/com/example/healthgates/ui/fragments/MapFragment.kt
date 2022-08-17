package com.example.healthgates.ui.fragments

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.VolleyLog
import com.android.volley.error.VolleyError
import com.example.healthgates.R
import com.example.healthgates.data.models.Branch
import com.example.healthgates.data.models.LabTest
import com.example.healthgates.databinding.FragmentBranchesBinding
import com.example.healthgates.databinding.FragmentMapBinding
import com.example.healthgates.ui.adapter.BranchAdapter
import com.example.healthgates.ui.adapter.BranchInterface
import com.example.healthgates.ui.base.BaseFragment
import com.example.healthgates.ui.interfaces.ApiListener
import com.example.healthgates.ui.viewmodel.PatientViewModel
import com.example.healthgates.utils.snackbar
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.json.JSONObject

class MapFragment : BaseFragment<FragmentMapBinding, PatientViewModel>() {

    private lateinit var branchAdapter: BranchAdapter
    private var userId: Int = 2

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    )= FragmentMapBinding.inflate(inflater, container, false)

    override fun getViewModel()= PatientViewModel::class.java

    override fun setupTheme(){

        binding.webView.settings.setJavaScriptEnabled(true)
        binding.webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, url: String): Boolean {
                view?.loadUrl(url)
                return true
            }
        }

        binding.webView.loadUrl(viewModel.getMapUrl()!!)
    }

    override fun setupClickListeners() {

    }

}