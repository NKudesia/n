package com.example.healthgates.ui.fragments

import android.Manifest
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.DocumentsContract
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.FileProvider
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.VolleyLog
import com.android.volley.error.VolleyError
import com.example.healthgates.R
import com.example.healthgates.data.models.*
import com.example.healthgates.databinding.FragmentAppointmentsBinding
import com.example.healthgates.databinding.FragmentHomeBinding
import com.example.healthgates.databinding.FragmentLabTestsBinding
import com.example.healthgates.ui.adapter.AppointmentAdapter
import com.example.healthgates.ui.adapter.AppointmentInterface
import com.example.healthgates.ui.adapter.LabTestAdapter
import com.example.healthgates.ui.adapter.LabTestInterface
import com.example.healthgates.ui.base.BaseFragment
import com.example.healthgates.ui.interfaces.ApiListener
import com.example.healthgates.ui.interfaces.DownloadListener
import com.example.healthgates.ui.viewmodel.PatientViewModel
import com.example.healthgates.utils.DownloadFileFromURLTask
import com.example.healthgates.utils.Popup
import com.example.healthgates.utils.loadImage
import com.example.healthgates.utils.snackbar
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject
import java.io.File

class RadResultsFragment : BaseFragment<FragmentLabTestsBinding, PatientViewModel>(), ApiListener,
    LabTestInterface {

    private lateinit var labTestAdapter: LabTestAdapter
    private lateinit var linearLayoutManager : LinearLayoutManager
    private var userId = 2
    private lateinit var selectedAppointment: Appointment
    private var labTestMode: Int = 0
    private var list: ArrayList<LabTest> = ArrayList()

    var downloadUrl = ""
    val storagePermission = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
    private val outputDir = "Healthgates"

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) =  FragmentLabTestsBinding.inflate(inflater, container, false)

    override fun getViewModel()= PatientViewModel::class.java

    override fun setupTheme() {

        lifecycleScope.launch(Dispatchers.Main) {
            userId = userPreferences.patientId.first()!!
            viewModel.getCurrentUser(userPreferences.userId.first()!!).observe(viewLifecycleOwner, {
                it?.let { userDetails ->
                    updateUI(userDetails)
                }
            })

                viewModel.getAllRadTestResults(userId, this@RadResultsFragment)
        }

        labTestAdapter = LabTestAdapter(requireContext(),this)
        linearLayoutManager = LinearLayoutManager(activity)
        binding.recyclerView.adapter = labTestAdapter
        binding.recyclerView.layoutManager = linearLayoutManager

    }


    override fun setupClickListeners() {

    }

    override fun onViewClick(labTest: LabTest) {
//        navController.navigate(R.id.action_homeFragment_to_bookAppointmentDateFragment)
        downloadUrl = labTest.report
        checkPermissions()
    }

    private fun updateUI(userDetails: UserDetails){
        binding.tvName.text = userDetails.name
        binding.ivProfile.loadImage(userDetails.image_1920)
    }


    private fun checkPermissions() {
        Dexter.withContext(requireActivity())
            .withPermissions(*storagePermission)
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                    report?.let {
                        when {
                            report.areAllPermissionsGranted() -> {
                                downloadFile()
                            }
                            report.isAnyPermissionPermanentlyDenied -> {
                                binding.root.snackbar("Required Permissions not granted")
                            }
                            else -> {
                                binding.root.snackbar("Required Permissions not granted")
                            }
                        }
                    }
                }

                override fun onPermissionRationaleShouldBeShown(permissions: MutableList<PermissionRequest>?, token: PermissionToken?) {
                    // Remember to invoke this method when the custom rationale is closed
                    // or just by default if you don't want to use any custom rationale.
                    token?.continuePermissionRequest()
                }
            })
            .withErrorListener {
                binding.root.snackbar(it.name)
            }
            .check()
    }

    private fun downloadFile() {
//        val downloadUrl = selectedAppointment.lab_report_url

        binding.root.snackbar("Download started")

        val download = DownloadFileFromURLTask(requireActivity(), downloadUrl, outputDir, object :
            DownloadListener {
            override fun onSuccess(path: String) {
                binding.root.snackbar("File is downloaded successfully at $path")

                val fileName: String = path.substring(path.lastIndexOf("/") + 1)
                openPDFDocument(context!!, fileName)
            }

            override fun onFailure(error: String) {
//                binding.root.snackbar(error)
                Popup(requireContext(),requireView(), "No file available")
            }
        })
        download.execute()
    }


    fun openPDFDocument(context: Context, fileName: String) {
        //Create PDF Intent
        var pdfFile = File(fileName)
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.R){
          pdfFile = File(Environment.getExternalStorageDirectory().absolutePath + "/Healthgates/Download/" + fileName)

        }else{
           pdfFile = File(Environment.getExternalStorageDirectory().absolutePath + "/Download/Healthgates/$fileName")
        }
        val pdfIntent = Intent(Intent.ACTION_VIEW)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            val uri = FileProvider.getUriForFile(
                context,
                context.getPackageName().toString() + ".provider",
                pdfFile
            )
            pdfIntent.setDataAndType(uri, "application/pdf")
            pdfIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }else {
            pdfIntent.setDataAndType(Uri.fromFile(pdfFile), "application/pdf")
        }
        //Create Viewer Intent
        val viewerIntent = Intent.createChooser(pdfIntent, "Open PDF")
        context.startActivity(viewerIntent)
    }

    override fun onSuccess(response: JSONObject) {
        Log.d(VolleyLog.TAG, "onSuccess: $response")
        if (response.has("result")) {
            if (response.get("result") is JSONArray) {
                val result = response.getJSONArray("result")

                val type = object : TypeToken<ArrayList<LabTest>>() {}.type

                list = Gson().fromJson(result.toString(), type)

                if(list.size == 0){
                    Popup(requireContext(),requireView(), resources.getString(R.string.dont_have_any_records))
                }else {
                    labTestAdapter.updateList(list)
                }
            }else{
                Popup(requireContext(),requireView(), resources.getString(R.string.dont_have_any_records))
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