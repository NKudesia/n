package com.example.healthgates.ui.fragments

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.VolleyLog
import com.android.volley.error.VolleyError
import com.example.healthgates.R
import com.example.healthgates.data.models.Branch
import com.example.healthgates.databinding.FragmentBranchesBinding
import com.example.healthgates.ui.adapter.BranchAdapter
import com.example.healthgates.ui.adapter.BranchInterface
import com.example.healthgates.ui.base.BaseFragment
import com.example.healthgates.ui.interfaces.ApiListener
import com.example.healthgates.ui.viewmodel.PatientViewModel
import com.example.healthgates.utils.Popup
import com.example.healthgates.utils.snackbar
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.json.JSONArray
import org.json.JSONObject

class BranchesFragment : BaseFragment<FragmentBranchesBinding, PatientViewModel>(), ApiListener,
    BranchInterface {

    private lateinit var branchAdapter: BranchAdapter
    private var userId: Int = 2

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    )= FragmentBranchesBinding.inflate(inflater, container, false)

    override fun getViewModel()= PatientViewModel::class.java

    override fun setupTheme(){
        branchAdapter = BranchAdapter(this@BranchesFragment)
        binding.recyclerView.adapter = branchAdapter
        binding.recyclerView.layoutManager = LinearLayoutManager(activity)

        viewModel.getAllBranches(userId, this@BranchesFragment)
    }

    override fun setupClickListeners() {

    }



    override fun onViewClick(branch: Branch, type: Int) {
        if(type == 0){
            viewModel.setSelectedBranch(branch)
            viewModel.setDoctorListMode(0)
            navController.navigate(R.id.action_branchesFragment_to_viewDoctorsFragment)
        }else if(type == 1){
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:"+branch.telephone)
            startActivity(intent)
//            checkPermission(branch)
        }else{
            if(branch.map_url != "false") {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(branch.map_url))
                requireActivity().startActivity(intent)
            }
//            viewModel.setMapUrl(branch.map_url)
//            navController.navigate(R.id.action_branchesFragment_to_mapFragment)
        }
    }


    fun checkPermission(branch: Branch) {
        if (context?.let {
                ContextCompat.checkSelfPermission(
                    it,
                    Manifest.permission.CALL_PHONE)
            }
            != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    Activity(),
                    Manifest.permission.CALL_PHONE)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(
                    Activity(),
                    arrayOf(Manifest.permission.CALL_PHONE),
                    42)
            }
        } else {
            // Permission has already been granted
            callPhone(branch)
        }
    }

    fun callPhone(branch: Branch){

        val intent = Intent(Intent.ACTION_CALL);
        intent.data = Uri.parse("tel:"+branch.telephone)
        startActivity(intent)
    }

    override fun onSuccess(response: JSONObject) {
        Log.d(VolleyLog.TAG, "onSuccess: $response")
        if (response.has("result")) {
            if (response.get("result") is JSONArray) {
                val result = response.getJSONArray("result")
//            val length = result.getInt("length")
//            val records = result.getJSONArray("records")
//            val list: ArrayList<Branch> = ArrayList()

                val type = object : TypeToken<ArrayList<Branch>>() {}.type
                val list: ArrayList<Branch> = Gson().fromJson(result.toString(), type)

                branchAdapter.updateList(list)
            }
        } else {
            Log.d(VolleyLog.TAG, "Error: ${response.getJSONObject("error").getString("message")}")
        }
    }

    override fun onFailure(error: VolleyError) {
        Log.d(VolleyLog.TAG, "Error ${error.message}")
//        binding.root.snackbar("Something went wrong! ${error.message}")
        Popup(requireContext(),requireView(), "Something Went Wrong! ${error.message}")
    }
}