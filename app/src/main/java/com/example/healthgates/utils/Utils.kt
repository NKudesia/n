package com.example.healthgates.utils

import android.content.Context
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.os.Build
import android.text.Html
import android.text.Spanned
import android.util.Base64
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.healthgates.R

fun ImageView.loadImage(drawableID: Int) {
    Glide
            .with(this.context)
            .load(drawableID)
            .transition(DrawableTransitionOptions.withCrossFade())
            .fitCenter()
            .into(this)
}

fun ImageView.loadImage(url: String) {
    if (url.isEmpty() || url == "false") {
        return
    }
    Glide
            .with(this.context)
            .load(Base64.decode(url, Base64.DEFAULT))
            .transition(DrawableTransitionOptions.withCrossFade())
            .circleCrop()
            .into(this)
}

fun Context.hasPermissions(permission: String?): Boolean {
    return ContextCompat.checkSelfPermission(this, permission!!) == PackageManager.PERMISSION_GRANTED
}

fun createMediaPlayer(context: Context, musicId: Int, isLooping: Boolean): MediaPlayer {
    val mediaPlayer = MediaPlayer.create(context, musicId)
    mediaPlayer.isLooping = isLooping
    return mediaPlayer
}

fun NavController.navigateUsingPopUp(popUpFragId: Int, destinationId: Int) {
    val navOptions = NavOptions.Builder()
            .setEnterAnim(R.anim.slide_in_right)
            .setExitAnim(R.anim.slide_out_left)
            .setPopEnterAnim(R.anim.slide_in_left)
            .setPopExitAnim(R.anim.slide_out_right)
            .setPopUpTo(popUpFragId, true)
            .build()
    navigate(destinationId, null, navOptions)
}

fun spannedFromHtml(text: String): Spanned? {
    return if (Build.VERSION.SDK_INT >= 24) {
        Html.fromHtml(text, Html.FROM_HTML_MODE_LEGACY)
    } else {
        @Suppress("DEPRECATION")
        Html.fromHtml(text)
    }
}