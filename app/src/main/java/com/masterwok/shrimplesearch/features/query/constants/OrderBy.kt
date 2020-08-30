package com.masterwok.shrimplesearch.features.query.constants

import android.content.Context
import com.masterwok.shrimplesearch.R

enum class OrderBy (val id: Int){
    Ascending(0),
    Descending(1);

    fun getDisplayValue(context: Context): String = when(this) {
        Ascending -> context.getString(R.string.order_by_asecnding)
        Descending -> context.getString(R.string.order_by_descending)
    }
}

