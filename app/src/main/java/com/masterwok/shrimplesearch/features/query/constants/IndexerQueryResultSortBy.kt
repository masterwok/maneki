package com.masterwok.shrimplesearch.features.query.constants

import android.content.Context
import com.masterwok.shrimplesearch.R

enum class IndexerQueryResultSortBy(val id: Int) {
    Name(0),
    Peers(1),
    Leechers(2),
    Size(3),
    PublishedOn(4);

    fun getDisplayValue(context: Context): String = when (this) {
        Name -> context.getString(R.string.component_sort_query_results_name)
        Peers -> context.getString(R.string.component_sort_query_results_peers)
        Leechers -> context.getString(R.string.component_sort_query_results_leechers)
        Size -> context.getString(R.string.component_sort_query_results_size)
        PublishedOn -> context.getString(R.string.component_sort_query_results_published_on)
    }
}
