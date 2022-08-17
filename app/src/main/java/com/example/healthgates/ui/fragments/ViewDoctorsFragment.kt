package com.example.healthgates.ui.fragments

import android.app.Dialog
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.android.volley.error.VolleyError
import com.example.healthgates.R
import com.example.healthgates.data.models.Branch
import com.example.healthgates.data.models.Doctor
import com.example.healthgates.data.models.Speciality
import com.example.healthgates.databinding.DialogLoadingProgressBinding
import com.example.healthgates.databinding.FragmentViewDoctorsBinding
import com.example.healthgates.ui.adapter.DoctorAdapterTwo
import com.example.healthgates.ui.adapter.DoctorsInterface
import com.example.healthgates.ui.base.BaseFragment
import com.example.healthgates.ui.interfaces.ApiListener
import com.example.healthgates.ui.viewmodel.PatientViewModel
import com.example.healthgates.utils.Popup
import com.example.healthgates.utils.createDialog
import com.example.healthgates.utils.snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.json.JSONObject

private const val TAG = "ViewDoctors"

class ViewDoctorsFragment: BaseFragment<FragmentViewDoctorsBinding, PatientViewModel>(),
    DoctorsInterface, ApiListener {

    private lateinit var doctorAdapterTwo: DoctorAdapterTwo
    private lateinit var loadingDialog: Dialog
    private var userId: Int = 2
    private var mode: Int = 0
    private var specialityList: ArrayList<Speciality> = ArrayList()
    private lateinit var speciality: Speciality
    private lateinit var selectedBranch: Branch

    override fun getFragmentBinding(inflater: LayoutInflater, container: ViewGroup?) = FragmentViewDoctorsBinding.inflate(inflater, container, false)

    override fun getViewModel() = PatientViewModel::class.java

    override fun setupTheme() {
        setupDialog()
        binding.spinner.lifecycleOwner = this

        doctorAdapterTwo = DoctorAdapterTwo(this)
        binding.recyclerView.adapter = doctorAdapterTwo

        lifecycleScope.launch(Dispatchers.Main) {
            userId = userPreferences.userId.first()!!
            mode = viewModel.getDoctorListMode()!!
            if(mode == 0) {
                selectedBranch = viewModel.getSelectedBranch()!!
            }

            viewModel.searchReadSpecialty(userId).observe(viewLifecycleOwner, {
                it?.let { specialistList ->
                    specialityList = specialistList
                    if (specialityList.isNotEmpty()) {
                        val list: List<String> = specialityList.map { speciality ->
                            speciality.name
                        }
                        Log.d(TAG, "spinner list $list")
                        binding.spinner.setItems(list)
                        binding.spinner.selectItemByIndex(0)
                    }
                }
            })
        }
    }

    private fun setupDialog() {
        val loadingDialogBinding = DialogLoadingProgressBinding.inflate(layoutInflater, binding.root, false)
        loadingDialog = createDialog(loadingDialogBinding, R.drawable.progress_circle, false)
    }

    override fun setupClickListeners() {
        binding.spinner.setOnSpinnerOutsideTouchListener { _, _ ->
            binding.spinner.dismiss()
        }
        binding.spinner.setOnSpinnerItemSelectedListener<String> { oldIndex, oldItem, newIndex, newItem ->
            val specialityId = specialityList[newIndex].id
            specialityId.let {
                if(mode == 0) {
                    viewModel.searchReadDoctorDetailsTwo(
                        userId,
                        specialityId,
                        selectedBranch.id,
                        mode,
                        this@ViewDoctorsFragment
                    )
                }else{
                    viewModel.searchReadDoctorDetailsTwo(
                        userId,
                        specialityId,
                        0,
                        mode,
                        this@ViewDoctorsFragment
                    )
                }
            }
        }
    }

    override fun onViewClick(doctor: Doctor) {
        viewModel.setSelectedDoctor(doctor)
        Log.d(TAG, "doctor $doctor")
        navController.navigate(R.id.action_viewDoctrosFragment_to_doctorFragment)
    }

    override fun onSuccess(response: JSONObject) {
        Log.d(TAG, "onSuccess: $response")
        if (response.has("result")) {
            val result = response.getJSONObject("result")
//            val length = result.getInt("length")
            val records = result.getJSONArray("records")
            val length = records.length()
            val list: ArrayList<Doctor> = ArrayList()

            for (i in 0 until length) {
                val record = records.getJSONObject(i)
                speciality = Speciality(
                    id = record.getJSONArray("specialty").get(0) as Int,
                    name = record.getJSONArray("specialty").get(1).toString()
                )
                list.add(Doctor(
                    id = record.getInt("id"),
                    user_id = record.getJSONArray("user_id").getInt(0),
//                    isOnline = record.getBoolean("is_online"),
                    profileImg = record.getString("image_1920"),
                    name = record.getString("name"),
//                        speciality = record.getJSONArray("specialty").get(1).toString()
                    speciality = speciality
                ))
            }
            doctorAdapterTwo.updateList(list)

        } else {
            Log.d(TAG, "Error: ${response.getJSONObject("error").getString("message")}")
        }
    }

    override fun onFailure(error: VolleyError) {
        Log.d(TAG, "Error ${error.message}")
//        binding.root.snackbar("Something went wrong! ${error.message}")
        Popup(requireContext(),requireView(), "Something Went Wrong! ${error.message}")
    }

}