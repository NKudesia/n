package com.example.healthgates.utils

import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import android.widget.TextView
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.lifecycleScope
import com.example.healthgates.R
import com.example.healthgates.data.preferences.UserPreferences
import com.example.healthgates.singleton.dataStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.healthgates.ui.interfaces.PopupListener
import kotlinx.coroutines.CoroutineScope

public class Popup2(val mContext:Context, view: View, private val text: String, private val popupListener: PopupListener) {

    private var popupWindow = PopupWindow()

    init {
        popupWindow = PopupWindow(text)
        popupWindow?.isOutsideTouchable = true
        popupWindow?.isFocusable = true
        popupWindow.showAtLocation(view, Gravity.CENTER, 0 , 0)
     }

    private fun PopupWindow(message: String): PopupWindow {
        val inflater = mContext?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.layout_popup_two, null)
        val tvMessage = view.findViewById<TextView>(R.id.tv_message)
        val tvAllowBtn = view.findViewById<TextView>(R.id.tv_allow_btn);
        val tvDenyBtn = view.findViewById<TextView>(R.id.tv_deny_btn);

        tvMessage.text = message

        tvAllowBtn.setOnClickListener(){
            allow()
        }


        tvDenyBtn.setOnClickListener(){
            dismissPopup()
        }

        return PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    private  fun allow(){
        popupWindow.dismiss()
        popupListener.onAllowButtonClick()
    }

    private  fun dismissPopup(){
        popupWindow.dismiss()
    }
}
