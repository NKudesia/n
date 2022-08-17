package com.example.healthgates.utils

import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import android.widget.TextView
import com.example.healthgates.R

public class Popup(val mContext:Context, view: View, private val text: String) {

    private var popupWindow = PopupWindow()

    init {
        popupWindow = PopupWindow(text)
        popupWindow?.isOutsideTouchable = true
        popupWindow?.isFocusable = true
        popupWindow.showAtLocation(view, Gravity.CENTER, 0 , 0)
    }

    private fun PopupWindow(message: String): PopupWindow {
        val inflater = mContext?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.layout_popup, null)
        val tvMessage = view.findViewById<TextView>(R.id.tv_message)
        val tvOkBtn = view.findViewById<TextView>(R.id.tv_ok_btn);

        tvMessage.text = message

        tvOkBtn.setOnClickListener(){
            dismissPopup()
        }

        return PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    private  fun dismissPopup(){
        popupWindow.dismiss()
    }
}
