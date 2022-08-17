package com.example.healthgates.utils

import android.graphics.Color
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import com.google.android.material.textview.MaterialTextView
import com.example.healthgates.R

fun View.snackbar(message: String) {
    Snackbar
            .make(this, message, Snackbar.LENGTH_LONG)
            .also { snackbar ->
                snackbar.setAction("Ok") {
                    snackbar.dismiss()
                }
                    .setAnchorView(R.id.nav_view)
                snackbar.view.findViewById<TextView>(com.google.android.material.R.id.snackbar_text).isSingleLine = false
            }.show()
}

fun RecyclerView.initLinearLayoutRecyclerView(orientation: Int) {
    with(this) {
        layoutManager = LinearLayoutManager(context, orientation, false)
        setHasFixedSize(true)
        isNestedScrollingEnabled = false
    }
}

fun RecyclerView.initGridLayoutRecyclerView(spanCount: Int, orientation: Int) {
    with(this) {
        layoutManager = GridLayoutManager(context, spanCount, orientation, false)
        setHasFixedSize(true)
        isNestedScrollingEnabled = false
    }
}

fun TextInputLayout.addTextWatcher() {
    val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            if (count == 0) {
                this@addTextWatcher.boxBackgroundColor = ContextCompat.getColor(this@addTextWatcher.context, R.color.white)
                this@addTextWatcher.boxStrokeColor = ContextCompat.getColor(this@addTextWatcher.context, R.color.white)
            } else if (count == 1) {
                this@addTextWatcher.boxBackgroundColor = ContextCompat.getColor(this@addTextWatcher.context, R.color.green)
                this@addTextWatcher.boxStrokeColor = ContextCompat.getColor(this@addTextWatcher.context, R.color.green)
            }
        }

        override fun afterTextChanged(s: Editable?) {}
    }
    this.editText?.addTextChangedListener(textWatcher)
}

fun TextInputLayout.showErrorOnEditText(errorMessage: String) {
    this.isErrorEnabled = true
    this.error = errorMessage
}

fun MaterialButton.enableButton() {
    this.isEnabled = true
    this.setTextColor(ContextCompat.getColor(this.context, R.color.white))
}

fun MaterialButton.disableButton() {
    this.isEnabled = false
    this.setTextColor(Color.argb(70, 255, 255, 255))
}

fun View.makeVisible() {
    this.visibility = View.VISIBLE
}

fun View.makeInvisible() {
    this.visibility = View.INVISIBLE
}

fun View.makeGone() {
    this.visibility = View.GONE
}