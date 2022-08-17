package com.example.healthgates.ui.fragments

import android.util.Log
import android.view.LayoutInflater
import android.view.SurfaceView
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.android.volley.error.VolleyError
import com.example.healthgates.R
import com.example.healthgates.data.config.AppConfig
import com.example.healthgates.databinding.FragmentVideoChatBinding
import com.example.healthgates.ui.base.BaseFragment
import com.example.healthgates.ui.interfaces.ApiListener
import com.example.healthgates.ui.viewmodel.PatientViewModel
import com.example.healthgates.utils.Popup
import com.example.healthgates.utils.snackbar
import io.agora.rtc.IRtcEngineEventHandler
import io.agora.rtc.RtcEngine
import io.agora.rtc.video.VideoCanvas
import io.agora.rtc.video.VideoEncoderConfiguration
import org.json.JSONObject

private const val TAG = "VideoChat"

class VideoChatFragment : BaseFragment<FragmentVideoChatBinding, PatientViewModel>() {

    private var mRtcEngine: RtcEngine? = null
    private var mCallEnd = false
    private var mMuted = false
    private var mLocalVideo: VideoCanvas? = null
    private var mRemoteVideo: VideoCanvas? = null

    override fun getFragmentBinding(inflater: LayoutInflater, container: ViewGroup?) = FragmentVideoChatBinding.inflate(inflater, container, false)

    override fun getViewModel() = PatientViewModel::class.java

    override fun onDestroy() {
        super.onDestroy()
        if (!mCallEnd) {
            leaveChannel()
        }
        RtcEngine.destroy()
        mRtcEngine = null
    }

    override fun setupTheme() {
        initAgoraEngineAndJoinChannel()
    }

    override fun setupClickListeners() {
//        binding.localVideoContainer.setOnClickListener { onLocalContainerClick() }
        binding.btnMute.setOnClickListener { onLocalAudioMuteClicked() }
        binding.endCallBtn.setOnClickListener { onEndCallClicked() }
        binding.switchCameraBtn.setOnClickListener { mRtcEngine!!.switchCamera() }
    }

    private fun initAgoraEngineAndJoinChannel() {
        initializeAgoraEngine()
        setupVideoConfig()
        setupLocalVideo()
        joinChannel()
    }

    private fun initializeAgoraEngine() {
        try {
            mRtcEngine = RtcEngine.create(requireActivity().baseContext, AppConfig.AGORA_APP_ID, mRtcEventHandler)
            Log.d(TAG, "initializeAgoraEngine: ${mRtcEngine}")
        } catch (e: Exception) {
            Log.d(TAG, Log.getStackTraceString(e))
            throw RuntimeException("NEaED TO check rtc sdk init fatal error\n" + Log.getStackTraceString(e))
        }
    }

    private fun setupVideoConfig() {
        mRtcEngine!!.enableVideo()

        mRtcEngine!!.setVideoEncoderConfiguration(
            VideoEncoderConfiguration(
                VideoEncoderConfiguration.VD_640x360,
                VideoEncoderConfiguration.FRAME_RATE.FRAME_RATE_FPS_15,
                VideoEncoderConfiguration.STANDARD_BITRATE,
                VideoEncoderConfiguration.ORIENTATION_MODE.ORIENTATION_MODE_FIXED_PORTRAIT
            )
        )
    }

    private fun setupLocalVideo() {
        // Create a SurfaceView object.
        val surfaceView = RtcEngine.CreateRendererView(requireActivity().baseContext)
        surfaceView.setZOrderMediaOverlay(true)
        binding.localVideoContainer.addView(surfaceView)

        // Set the local video view.
        mRtcEngine!!.setupLocalVideo(VideoCanvas(surfaceView, VideoCanvas.RENDER_MODE_HIDDEN, 0))
    }

    private fun joinChannel() {
        // Join a channel with a token.
        mRtcEngine!!.joinChannel(viewModel.getTokenId(), viewModel.getChannelId(), getString(R.string.app_name), 0)
    }

    private fun onLocalAudioMuteClicked() {
        mMuted = !mMuted
        // Stops/Resumes sending the local audio stream.
        mRtcEngine!!.muteLocalAudioStream(mMuted)
        val res: Int = if (mMuted) R.drawable.ic_mic_off else R.drawable.ic_mic_on
        binding.btnMute.setImageResource(res)
    }

    private fun onEndCallClicked() {
        if (mCallEnd) {
            startCall()
            mCallEnd = false
            binding.endCallBtn.setImageResource(R.drawable.ic_call_end)
            binding.endCallBtn.backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.red)
        } else {
            endCall()
            mCallEnd = true
            binding.endCallBtn.setImageResource(R.drawable.ic_call)
            binding.endCallBtn.backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.green)
        }
        showButtons(!mCallEnd)
    }

    private fun showButtons(show: Boolean) {
        val visibility = if (show) View.VISIBLE else View.GONE
        binding.btnMute.visibility = visibility
        binding.switchCameraBtn.visibility = visibility
    }

    private fun onLocalContainerClick() {
        switchView(mLocalVideo!!)
        switchView(mRemoteVideo!!)
    }

    private fun switchView(canvas: VideoCanvas) {
        val parent = removeFromParent(canvas)
        if (parent == binding.localVideoContainer) {
            if (canvas.view is SurfaceView) {
                (canvas.view as SurfaceView).setZOrderMediaOverlay(false)
            }
            binding.remoteVideoContainer.addView(canvas.view)

        } else if (parent == binding.remoteVideoContainer) {
            if (canvas.view is SurfaceView) {
                (canvas.view as SurfaceView).setZOrderMediaOverlay(true)
            }
            binding.localVideoContainer.addView(canvas.view)
        }
    }

    private fun removeFromParent(canvas: VideoCanvas?): ViewGroup? {
        if (canvas != null) {
            val parent = canvas.view.parent
            if (parent != null) {
                val group = parent as ViewGroup
                group.removeView(canvas.view)
                return group
            }
        }
        return null
    }

    private fun startCall() {
        setupLocalVideo()
        joinChannel()
    }

    private fun endCall() {
        removeFromParent(mLocalVideo)
        mLocalVideo = null
        removeFromParent(mRemoteVideo)
        mRemoteVideo = null
        leaveChannel()
        disconnectCall()
    }

    private fun leaveChannel() {
        // Leave the current channel.
        mRtcEngine!!.leaveChannel()
    }


    /// Remote User
    private val mRtcEventHandler = object : IRtcEngineEventHandler() {

        override fun onJoinChannelSuccess(channel: String?, uid: Int, elapsed: Int) {
            super.onJoinChannelSuccess(channel, uid, elapsed)
            Log.d(TAG, "onJoinChannelSuccess: $channel  UID: $uid  elapsed $elapsed ")

//            val doctorId = viewModel.getSelectedDoctor()!!.speciality.id
            val doctorId = viewModel.getSelectedDoctor()!!.user_id
            viewModel.incomingCallToDoctor(doctorId, object : ApiListener {
                override fun onSuccess(response: JSONObject) {
                    Log.d(TAG, "onSuccess: $response")
                }

                override fun onFailure(error: VolleyError) {
                    Log.d(TAG, "Error ${error.message}")
//                    binding.root.snackbar("Something went wrong! ${error.message}")
                    Popup(requireContext(),requireView(), "Something Went Wrong! ${error.message}")
                }
            })
        }

        // Listen for the onUserJoined callback.
        // This callback occurs when the remote user successfully joins the channel.
        // You can call the setupRemoteVideo method in this callback to set up the remote video view.
        override fun onUserJoined(uid: Int, elapsed: Int) {
            Log.d(TAG, "onUserJoined: $uid  elapsed $elapsed ")
            requireActivity().runOnUiThread {
                setupRemoteVideo(uid)
            }
        }

        // Listen for the onUserOffline callback.
        // This callback occurs when the remote user leaves the channel or drops offline.
        override fun onUserOffline(uid: Int, reason: Int) {
            Log.d(TAG, "onUserOffline: $uid  Reason: $reason")
            requireActivity().runOnUiThread {
                onRemoteUserLeft(uid)
            }
        }

    }

    private fun setupRemoteVideo(uid: Int) {
        var parentViewGroup: ViewGroup = binding.remoteVideoContainer
        if (parentViewGroup.indexOfChild(mLocalVideo?.view) > -1) {
            parentViewGroup = binding.localVideoContainer
        }

        if (mRemoteVideo != null) {
            return
        }

        // Create a SurfaceView object.
        val surfaceView = RtcEngine.CreateRendererView(requireActivity().baseContext)
        surfaceView.setZOrderMediaOverlay(parentViewGroup == binding.localVideoContainer)
        parentViewGroup.addView(surfaceView)

        // Set the remote video view.
        mRtcEngine!!.setupRemoteVideo(VideoCanvas(surfaceView, VideoCanvas.RENDER_MODE_FIT, uid))
    }

    private fun disconnectCall(){
        val doctorId = viewModel.getSelectedDoctor()!!.user_id
        viewModel.disconnectCall(doctorId, object : ApiListener {
            override fun onSuccess(response: JSONObject) {
                Log.d(TAG, "onSuccess: $response")
            }

            override fun onFailure(error: VolleyError) {
                Log.d(TAG, "Error ${error.message}")
//                binding.root.snackbar("Something went wrong! ${error.message}")
                Popup(requireContext(),requireView(), "Something Went Wrong! ${error.message}")
            }
        })
    }

    private fun onRemoteUserLeft(uid: Int) {
        if (mRemoteVideo?.uid == uid) {
            removeFromParent(mRemoteVideo)
            // Destroys remote view
            mRemoteVideo = null
        }
    }

}