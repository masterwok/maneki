package com.masterwok.shrimplesearch.features.query.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.masterwok.shrimplesearch.R


class IndexerQueryResultsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(
        R.layout.fragment_indexer_query_results
        , container
        , false
    )

    companion object {
        @JvmStatic
        fun newInstance() = IndexerQueryResultsFragment()
    }
}
