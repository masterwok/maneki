package com.masterwok.shrimplesearch.common.extensions

import android.content.Context
import androidx.core.os.ConfigurationCompat
import java.text.NumberFormat
import java.util.*


fun Context.getCurrentLocale(): Locale = ConfigurationCompat
    .getLocales(resources.configuration)
    .get(0)

fun Context.getLocaleNumberFormat(): NumberFormat = NumberFormat
    .getNumberInstance(getCurrentLocale())