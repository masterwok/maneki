package com.masterwok.shrimplesearch.features.query.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter;
import com.masterwok.shrimplesearch.features.query.fragments.IndexerQueryResultsFragment
import com.masterwok.shrimplesearch.features.query.fragments.QueryFragment

class QueryFragmentStateAdapter(
    fragmentManager: FragmentManager
    , lifecycle: Lifecycle
) : FragmentStateAdapter(
    fragmentManager
    , lifecycle
) {

    private val fragmentCollection = listOf(
        lazy { QueryFragment.newInstance() },
        lazy { IndexerQueryResultsFragment.newInstance() }
    )

    override fun getItemCount(): Int {
        return fragmentCollection.count()
    }

    override fun createFragment(position: Int): Fragment {
        return fragmentCollection[position].value
    }

}
