package com.masterwok.shrimplesearch.features.query.constants

import android.content.Context
import com.masterwok.shrimplesearch.R

enum class QueryResultSortBy(val id: Int) {
    Name(0),
    MagnetCount(1),
    LinkCount(2),
    AggregateCount(3);

    fun getDisplayValue(context: Context): String = when (this) {
        Name -> context.getString(R.string.query_result_sort_by_name)
        MagnetCount -> context.getString(R.string.query_result_sort_by_magnet_count)
        LinkCount -> context.getString(R.string.query_result_sort_by_link_count)
        AggregateCount -> context.getString(R.string.query_result_sort_by_aggregate_count)
    }

    companion object {
        fun getByValue(value: Int) = values().first { it.id == value }
    }

}
