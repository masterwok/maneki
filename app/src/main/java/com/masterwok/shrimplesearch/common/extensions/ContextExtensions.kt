package com.masterwok.shrimplesearch.common.extensions

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.os.ConfigurationCompat
import java.text.NumberFormat
import java.util.*


fun Context.getCurrentLocale(): Locale = ConfigurationCompat
    .getLocales(resources.configuration)
    .get(0)

fun Context.getLocaleNumberFormat(): NumberFormat = NumberFormat
    .getNumberInstance(getCurrentLocale())


@SuppressLint("ObsoleteSdkInt")
fun Context.startPlayStoreActivity() {

    val intent = if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.LOLLIPOP) {
        Intent(
            Intent.ACTION_VIEW,
            Uri.parse("http://play.google.com/store/apps/details?id=$packageName")
        )
    } else {
        Intent(
            Intent.ACTION_VIEW,
            Uri.parse("market://details?id=$packageName")
        ).apply {
            flags = Intent.FLAG_ACTIVITY_NO_HISTORY or
                    Intent.FLAG_ACTIVITY_NEW_DOCUMENT or
                    Intent.FLAG_ACTIVITY_MULTIPLE_TASK
        }
    }

    startActivity(intent)
}