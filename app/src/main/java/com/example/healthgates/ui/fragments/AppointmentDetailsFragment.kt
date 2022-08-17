package com.example.healthgates.ui.fragments

import android.Manifest
import android.app.AlertDialog
import android.app.Dialog
import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Context.DOWNLOAD_SERVICE
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.DocumentsContract
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.content.FileProvider
import androidx.lifecycle.lifecycleScope
import com.android.volley.VolleyLog
import com.android.volley.error.VolleyError
import com.bumptech.glide.Glide
import com.example.healthgates.R
import com.example.healthgates.data.models.*
import com.example.healthgates.databinding.DialogLoadingProgressBinding
import com.example.healthgates.databinding.FragmentAppointmentDetailsBinding
import com.example.healthgates.databinding.FragmentProfileBinding
import com.example.healthgates.ui.adapter.AppointmentDetailAdapter
import com.example.healthgates.ui.adapter.ProfileScreenAdapter
import com.example.healthgates.ui.base.BaseFragment
import com.example.healthgates.ui.interfaces.ChannelIdApiListener
import com.example.healthgates.ui.interfaces.DownloadListener
import com.example.healthgates.ui.interfaces.PopupListener
import com.example.healthgates.ui.viewmodel.PatientViewModel
import com.example.healthgates.utils.*
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.selects.select
import org.json.JSONObject
import java.io.File
import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class AppointmentDetailsFragment : BaseFragment<FragmentAppointmentDetailsBinding, PatientViewModel>(),
    PopupListener {

    private lateinit var appointDetailAdapter: AppointmentDetailAdapter
    private var userId = 2
    private lateinit var selectedAppointment: Appointment
    private lateinit var appointDetailAdapterTwo: AppointmentDetailAdapter

    private lateinit var loadingDialog: Dialog

    private lateinit var speciality: Speciality

    private var fileAccess = false

    private var diagnosis = ""

    var downloadPage=""
    var downloadUrl = ""

    val storagePermission = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
    private val outputDir = "Healthgates"

    private val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    private val dateFormat2 = SimpleDateFormat("dd-MM-yyyy")
    private val dateFormat3 = SimpleDateFormat("hh:mm a")

    override fun getFragmentBinding(inflater: LayoutInflater, container: ViewGroup?) = FragmentAppointmentDetailsBinding.inflate(inflater, container, false)

    override fun getViewModel() = PatientViewModel::class.java

    //    @SuppressLint("ClickableViewAccessibility")
    override fun setupTheme() {

        selectedAppointment = viewModel.getSelectedAppointment()!!

        initRecyclerView()

        lifecycleScope.launch(Dispatchers.Main) {
            userId = userPreferences.patientId.first()!!
            viewModel.getCurrentUser(userPreferences.userId.first()!!).observe(viewLifecycleOwner, {
                it?.let { userDetails ->
//                    updateUI(userDetails)
                }
            })

            fileAccess = userPreferences.fileAccess.first() ?: false
        }

        binding.tvName.text = selectedAppointment.physician_name
        binding.tvSpeciality.text = selectedAppointment.speciality_name
        Glide.with(this)
            .load(selectedAppointment.physician_image)
            .into(binding.ivProfile)

        val date = dateFormat.parse(selectedAppointment.date)
        binding.tvDate.text = dateFormat2.format(date)
        binding.tvTime.text = dateFormat3.format(date)

        if(selectedAppointment.booked_online){
            binding.videoCall.setImageResource(R.drawable.ic_videocam_online)
        }

        if(selectedAppointment.from_slot != "false" && selectedAppointment.to_slot != "false") {
            val date2 = Date()
            val date3 = dateFormat.parse(selectedAppointment.from_slot)
            val date4 = dateFormat.parse(selectedAppointment.to_slot)

            if (date2.before(date3) || date2.after(date4)) {
                binding.videoCall.visibility = View.GONE
            }
        }else{
            binding.videoCall.visibility = View.GONE
        }

        binding.videoCall.visibility = View.GONE

        for (i in 0 until selectedAppointment.diagnosis.size){

            this.diagnosis += selectedAppointment.diagnosis[i].name
            if(i < selectedAppointment.diagnosis.size - 1){
                this.diagnosis += ", "
            }
        }

        binding.tvDiagnosis.text = diagnosis

        setupDialog()
    }

    private fun setupDialog() {
        val loadingDialogBinding = DialogLoadingProgressBinding.inflate(layoutInflater, binding.root, false)
        loadingDialog = createDialog(loadingDialogBinding, R.drawable.progress_circle, false)
    }


    override fun setupClickListeners() {

        binding.cvLabTests.setOnClickListener() {
//            if (selectedAppointment.test_count > 0) {
//                viewModel.setLabTestMode(1)
//                navController.navigate(R.id.action_appointmentDetailsFragment_to_labTestsFragment)
//            }

//            Log.d("Url", selectedAppointment.lab_report_url)

            downloadUrl = selectedAppointment.lab_report_url


            if (fileAccess == true) {
                checkPermissions()
            } else {

                Popup2(
                    requireContext(),
                    requireView(),
                    "Healthgates app need Read and Write permissions to your phone's external storage. Write permission is required to save lab test reports, prescriptions and other important documents. Read permission is required to open and display the saved documents within app. ",
                    this
                )
            }
//            downloadPage= selectedAppointment.lab_report_url;
        }

        binding.cvPrescriptions.setOnClickListener() {
//            if (selectedAppointment.test_count > 0) {
//                viewModel.setLabTestMode(1)
//                navController.navigate(R.id.action_appointmentDetailsFragment_to_labTestsFragment)
//            }

//            Log.d("Url", selectedAppointment.prescription_report_url)
            downloadUrl = selectedAppointment.prescription_report_url

            if (fileAccess == true) {
                checkPermissions()
            } else {

                Popup2(
                    requireContext(),
                    requireView(),
                    "Healthgates app need Read and Write permissions to your phone's external storage. Write permission is required to save lab test reports, prescriptions and other important documents. Read permission is required to open and display the saved documents within app. ",
                    this
                )
            }
//            downloadPage= selectedAppointment.lab_report_url;
        }

        binding.videoCall.setOnClickListener(){
            if(selectedAppointment.booked_online) {
                callDoctor()
            }
        }

    }

    private fun initRecyclerView() {
        appointDetailAdapter = AppointmentDetailAdapter()
        binding.recyclerView.adapter = appointDetailAdapter

        appointDetailAdapterTwo = AppointmentDetailAdapter()
        binding.recyclerViewTwo.adapter = appointDetailAdapterTwo

        updateAppointmentScreenList()
    }

    private fun updateUI(userDetails: UserDetails) {
//        binding.ivProfile.loadImage(userDetails.image_1920)
         }

    private fun updateAppointmentScreenList() {
        val list: ArrayList<ProfileItem> = ArrayList()
        val list2: ArrayList<ProfileItem> = ArrayList()
        with(list) {
            add(ProfileItem(sideDividerVisible = true, value = selectedAppointment.temprature?:"", type = getString(R.string.temprature)))
            add(ProfileItem(sideDividerVisible = true, value = selectedAppointment.weight?:"" , type = getString(R.string.weight)))
            if(selectedAppointment.blood_group == "false"){
                add(
                    ProfileItem(
                        sideDividerVisible = false,
                        value = "",
                        type = getString(R.string.blood_type)
                    )
                )
            }else {
                add(
                    ProfileItem(
                        sideDividerVisible = false,
                        value = selectedAppointment.blood_group ?: "",
                        type = getString(R.string.blood_type)
                    )
                )
            }
        }
        with(list2) {
            add(ProfileItem(sideDividerVisible = true, value = selectedAppointment.systolic_blood_pressure?:"", type = getString(R.string.systolic)))
            add(ProfileItem(sideDividerVisible = true, value = selectedAppointment.diastolic_blood_pressure?:"", type = getString(R.string.distolic)))
            add(ProfileItem(sideDividerVisible = false, value = selectedAppointment.heart_rate_per_minute?:"", type = getString(R.string.heart_rate)))
        }
        appointDetailAdapter.updateList(list)
        appointDetailAdapterTwo.updateList(list2)
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
//        val pdfFile = File(Environment.getExternalStorageDirectory().absolutePath + "/Healthgates/" + fileName)
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.R){
            pdfFile = File(Environment.getExternalStorageDirectory().absolutePath + "/Healthgates/Download/" + fileName)

        }else{
            pdfFile = File(Environment.getExternalStorageDirectory().absolutePath + "/Download/Healthgates/$fileName")
        }
//        val pdfFile = File(path)
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

    private fun checkAllPermissions(): Boolean {
        if (!requireContext().hasPermissions(Manifest.permission.RECORD_AUDIO)) {
            return false
        }
        return requireContext().hasPermissions(Manifest.permission.CAMERA)
    }

    private fun callDoctor() {
        if (!checkAllPermissions()) {
            requestPermissions(arrayOf(Manifest.permission.RECORD_AUDIO, Manifest.permission.CAMERA), 101)
            return
        }
//        if (!selectedDoctor.isOnline) {
//            binding.root.snackbar("The doctor is currently not available!")
//            return
//        }
        loadingDialog.show()

        viewModel.generateTokenAndChannelId(selectedAppointment.physician_user_id, userId, object :
            ChannelIdApiListener {
            override fun onSuccess(response: JSONObject, tokenId: String, channelId: String) {
                loadingDialog.dismiss()
                if (response.has("result")) {
                    val result = response.getJSONObject("result")
                    if (result.getBoolean("success")) {

                        if(selectedAppointment.speciality_id != null && selectedAppointment.speciality_name != null){
                            speciality = Speciality(
                                id = selectedAppointment.speciality_id,
                                name = selectedAppointment.speciality_name
                            )
                        }else {
                            speciality = Speciality(
                                id = 0,
                                name = ""
                            )
                        }

                        val doctor = Doctor(
                            id = selectedAppointment.physician_id,
                            user_id = selectedAppointment.physician_user_id,
                            name = selectedAppointment.physician_name,
                            speciality = speciality
                        )

                        viewModel.setSelectedDoctor(doctor)
                        viewModel.setChannelIdAndTokenId(channelIdValue = channelId, tokenIdValue = tokenId)
                        navController.navigate(R.id.action_appointmentDetailsFragment_to_videoChatFragment)
                    } else {
//                        binding.root.snackbar("Something Went Wrong! ${result.getString("msg")}")
                        Popup(requireContext(),requireView(), "Something Went Wrong! ${result.getString("msg")}")
                    }
                } else {
//                    binding.root.snackbar("Something Went Wrong!")
                    Popup(requireContext(),requireView(), "Something Went Wrong!")
                }
            }

            override fun onFailure(error: VolleyError) {
                loadingDialog.dismiss()
//                binding.root.snackbar("Something went wrong! ${error.message}")
                Popup(requireContext(),requireView(), "Something Went Wrong! ${error.message}")
            }
        })
    }

    override fun onAllowButtonClick() {
        lifecycleScope.launch(Dispatchers.Main) {
            userPreferences.saveFileAccess(true)
            fileAccess = true
            checkPermissions()
        }
    }


}