package com.example.healthgates.utils

import android.app.Dialog
import android.content.Context
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.viewbinding.ViewBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.example.healthgates.R
import com.example.healthgates.ui.interfaces.AlertDialogCallback
import kotlinx.coroutines.NonCancellable.cancel
import java.lang.System.exit

fun createDialog(layoutResViewBinding: ViewBinding, drawableID: Int, cancellable: Boolean): Dialog {
    val dialog = Dialog(layoutResViewBinding.root.context)
    with(dialog) {
        setContentView(layoutResViewBinding.root)
        setCancelable(cancellable)
        window?.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        window?.setBackgroundDrawable(ContextCompat.getDrawable(layoutResViewBinding.root.context, drawableID))
    }
    return dialog
}

fun showAlertDialog(context: Context, title: String, subTitle: String, listener: AlertDialogCallback) {
    val resources = context.resources

    MaterialAlertDialogBuilder(context)
            .setTitle(title)
            .setMessage(subTitle)
            .setCancelable(true)
            .setNegativeButton(resources.getString(R.string.cancel)) { dialog, _ ->
                listener.onNegativeButtonClick(dialog = dialog)
            }
            .setPositiveButton(resources.getString(R.string.exit)) { _, _ ->
                listener.onPositiveButtonClick()
            }
            .show()
}