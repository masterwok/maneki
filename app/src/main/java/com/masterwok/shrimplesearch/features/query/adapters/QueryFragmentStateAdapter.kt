package com.masterwok.shrimplesearch.features.query.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter;
import com.masterwok.shrimplesearch.features.query.fragments.IndexerQueryResultsFragment
import com.masterwok.shrimplesearch.features.query.fragments.QueryFragment
import kotlin.reflect.KClass

class QueryFragmentStateAdapter(
    fragmentManager: FragmentManager
    , lifecycle: Lifecycle
) : FragmentStateAdapter(
    fragmentManager
    , lifecycle
) {

    private val fragmentMap = mapOf(
        QueryFragment::class to lazy { QueryFragment.newInstance() },
        IndexerQueryResultsFragment::class to lazy { IndexerQueryResultsFragment.newInstance() }
    )

    override fun getItemCount(): Int {
        return fragmentMap.count()
    }

    override fun createFragment(position: Int): Fragment = fragmentMap
        .values
        .toTypedArray()[position]
        .value

    fun getFragmentIndex(kClass: KClass<*>): Int = fragmentMap
        .entries
        .indexOfFirst { it.key == kClass }

}
