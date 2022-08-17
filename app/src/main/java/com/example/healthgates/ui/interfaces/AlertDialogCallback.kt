package com.example.healthgates.ui.interfaces;

import android.content.DialogInterface;

interface AlertDialogCallback {
    fun onNegativeButtonClick(dialog: DialogInterface)
    fun onPositiveButtonClick()
}