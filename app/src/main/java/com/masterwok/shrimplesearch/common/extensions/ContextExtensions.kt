package com.masterwok.shrimplesearch.common.extensions

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.TypedValue
import androidx.annotation.AttrRes
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.os.ConfigurationCompat
import java.text.NumberFormat
import java.util.*


fun Context.getCurrentLocale(): Locale = ConfigurationCompat
    .getLocales(resources.configuration)
    .get(0)

fun Context.getLocaleNumberFormat(): NumberFormat = NumberFormat
    .getNumberInstance(getCurrentLocale())

@RequiresApi(api = android.os.Build.VERSION_CODES.LOLLIPOP)
fun Context.getPlayStoreUri(): Uri = Uri
    .parse("http://play.google.com/store/apps/details?id=$packageName")

@SuppressLint("ObsoleteSdkInt")
fun Context.startPlayStoreActivity() {

    val intent = if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.LOLLIPOP) {
        Intent(
            Intent.ACTION_VIEW,
            getPlayStoreUri()
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


fun Context.getColorByAttribute(@AttrRes attributeResourceId: Int): Int = TypedValue().apply {
    theme.resolveAttribute(
        attributeResourceId,
        this,
        true
    )
}.data


fun Context.copyToClipboard(label: String, text: String) {
    (getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager).apply {
        setPrimaryClip(ClipData(label, emptyArray(), ClipData.Item(text)))
    }
}