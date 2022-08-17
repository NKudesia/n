package com.example.healthgates.ui.fragments

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import com.android.volley.VolleyLog.TAG
import com.android.volley.error.VolleyError
import com.example.healthgates.R
import com.example.healthgates.data.models.*
import com.example.healthgates.databinding.FragmentHomeBinding
import com.example.healthgates.ui.adapter.DoctorAdapter
import com.example.healthgates.ui.adapter.DoctorsInterface
import com.example.healthgates.ui.adapter.HomeAdapter
import com.example.healthgates.ui.adapter.HomeInterface
import com.example.healthgates.ui.base.BaseFragment
import com.example.healthgates.ui.interfaces.ApiListener
import com.example.healthgates.ui.viewmodel.PatientViewModel
import com.example.healthgates.utils.Popup
import com.example.healthgates.utils.loadImage
import com.example.healthgates.utils.snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject

class HomeFragment : BaseFragment<FragmentHomeBinding, PatientViewModel>(), DoctorsInterface,
    ApiListener, HomeInterface {

    private lateinit var homeAdapter: HomeAdapter
    private lateinit var DoctorAdapter: DoctorAdapter
    private lateinit var linearLayoutManager : LinearLayoutManager
    private var userId: Int = 2
    private lateinit var speciality: Speciality

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) =  FragmentHomeBinding.inflate(inflater, container, false)

    override fun getViewModel()= PatientViewModel::class.java

    override fun setupTheme() {

        homeAdapter = HomeAdapter(this)
        binding.recyclerView.adapter = homeAdapter
//        binding.recyclerView.layoutManager = GridLayoutManager(activity, 2)
        binding.recyclerView.layoutManager = LinearLayoutManager(activity)

        lifecycleScope.launch(Dispatchers.Main){
            userId = userPreferences.userId.first()!!
            viewModel.getCurrentUser(userPreferences.userId.first()!!).observe(viewLifecycleOwner) {
                it?.let { userDetails ->
                    lifecycleScope.launch(Dispatchers.Main) {
                        userPreferences.savePatientId(userDetails.id)
                    }
                    updateUI(userDetails)
                }
            }
        }

        DoctorAdapter = DoctorAdapter(this)
        linearLayoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL,false)
        binding.recyclerViewCallDoctor.adapter = DoctorAdapter
        binding.recyclerViewCallDoctor.layoutManager = linearLayoutManager
        val snapHelper = PagerSnapHelper()
        snapHelper.attachToRecyclerView(binding.recyclerViewCallDoctor)
        updateAdapterList()

        viewModel.searchReadDoctorDetails(userId, this@HomeFragment)

    }


    override fun setupClickListeners() {

        binding.cardView.setOnClickListener(){
            navController.navigate(R.id.action_homeFragment_to_profileFragment)
        }

        binding.ivArrowLeft.setOnClickListener {
            if (linearLayoutManager.findLastCompletelyVisibleItemPosition() > 0 && DoctorAdapter.itemCount > 1) {
                linearLayoutManager.scrollToPosition(linearLayoutManager.findLastCompletelyVisibleItemPosition() - 1)
            }
        }

        binding.ivArrowRight.setOnClickListener {
            if (linearLayoutManager.findLastCompletelyVisibleItemPosition() < DoctorAdapter.itemCount - 1) {
                linearLayoutManager.scrollToPosition(linearLayoutManager.findLastCompletelyVisibleItemPosition() + 1)
            }
        }

    }

    override fun onViewClick(position: Int){
        if(position == 0){
            navController.navigate(R.id.action_homeFragment_to_appointmentsFragment)
        }else if(position == 1){
            navController.navigate(R.id.action_homeFragment_to_prescriptionsFragment)
        }else if(position == 2){
            viewModel.setLabTestMode(0)
            navController.navigate(R.id.action_homeFragment_to_labTestsFragment)
        }else if(position == 3){
            navController.navigate(R.id.action_homeFragment_to_radResultsFragment)
        }
    }

    override fun onViewClick(doctor: Doctor) {
        viewModel.setSelectedDoctor(doctor)
        navController.navigate(R.id.action_homeFragment_to_doctorFragment)
    }

    private fun updateUI(userDetails: UserDetails){
        binding.tvName.text = userDetails.name
        binding.tvMrn.text = userDetails.code
        binding.ivProfile.loadImage(userDetails.image_1920)
    }

    private fun updateAdapterList() {
        val list: ArrayList<HomeScreenItem> = ArrayList()
        with(list) {
            add(HomeScreenItem(backgroundColor = R.color.home_screen_bg_purplish, image = R.drawable.icons_file_records, text = getString(R.string.appointments), textColor = "#FFFFFF"))
            add(HomeScreenItem(backgroundColor = R.color.home_screen_bg_blueish, image = R.drawable.icons_medicine, text = getString(R.string.my_prescriptions), textColor = "#FFFFFF"))
//            add(HomeScreenItem(backgroundColor = R.color.home_screen_bg_light_blueish, image = R.drawable.ic_local_atm_24px, text = getString(R.string.my_invoices), textColor = "#FFFFFF"))
//            add(HomeScreenItem(backgroundColor = R.color.home_screen_bg_medium_blueish, image = R.drawable.icons_medicine, text = getString(R.string.my_prescriptions), textColor = "#FFFFFF"))
            add(HomeScreenItem(backgroundColor = R.color.home_screen_bg_medium_greenish, image = R.drawable.icons_microscope, text = getString(R.string.lab_test_results), textColor = "#FFFFFF"))
            add(HomeScreenItem(backgroundColor = R.color.home_screen_bg_greenish, image = R.drawable.icons_file_records, text = getString(R.string.rad_results), textColor = "#FFFFFF"))
           }
        homeAdapter.updateList(list)

    }


    override fun onSuccess(response: JSONObject) {
        Log.d(TAG, "onSuccess: $response")
        if (response.has("result")) {
            val result = response.getJSONObject("result")
//            val length = result.getInt("length")
            val records = result.getJSONArray("records")
            val length = records.length()
            var list: ArrayList<Doctor> = ArrayList()

//            val type = object: TypeToken<ArrayList<Doctor>>() {}.type
//            list = Gson().fromJson(records.toString(), type)

            for (i in 0 until length) {
                val record = records.getJSONObject(i)

                if(record["specialty"] is JSONArray) {
                    speciality = Speciality(
                        id = record.getJSONArray("specialty").get(0) as Int,
                        name = record.getJSONArray("specialty").get(1).toString()
                    )
                }else{
                    speciality = Speciality(
                        id = 0,
                        name = ""
                    )
                }

                list.add(
                    Doctor(
                    id = record.getInt("id"),
                    user_id = record.getJSONArray("user_id").getInt(0),
//                    isOnline = record.getBoolean("is_online"),
                    profileImg = record.getString("image_1920"),
                    name = record.getString("name"),
//                        speciality = record.getJSONArray("specialty").get(1).toString()
                    speciality = speciality
                )
                )
            }
            DoctorAdapter.updateList(list)
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