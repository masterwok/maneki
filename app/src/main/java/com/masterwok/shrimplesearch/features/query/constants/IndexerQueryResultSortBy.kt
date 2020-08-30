package com.masterwok.shrimplesearch.features.query.constants

import android.content.Context
import com.masterwok.shrimplesearch.R

enum class IndexerQueryResultSortBy(val id: Int) {
    Name(0),
    Peers(1),
    Seeders(2),
    Size(3),
    PublishedOn(4);

    fun getDisplayValue(context: Context): String = when (this) {
        Name -> context.getString(R.string.component_sort_query_results_name)
        Peers -> context.getString(R.string.component_sort_query_results_peers)
        Seeders -> context.getString(R.string.component_sort_query_results_seeders)
        Size -> context.getString(R.string.component_sort_query_results_size)
        PublishedOn -> context.getString(R.string.component_sort_query_results_published_on)
    }

    companion object {
        fun getByValue(value: Int) = values().first { it.id == value }
    }

}
